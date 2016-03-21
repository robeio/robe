package io.robe.admin.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.ActionLogDao;
import io.robe.admin.hibernate.dao.MailTemplateDao;
import io.robe.admin.hibernate.dao.TicketDao;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.ActionLog;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.admin.hibernate.entity.User;
import io.robe.admin.util.SystemParameterCache;
import io.robe.admin.util.TemplateManager;
import io.robe.auth.AbstractAuthResource;
import io.robe.auth.Credentials;
import io.robe.auth.token.TokenManager;
import io.robe.auth.tokenbased.BasicToken;
import io.robe.auth.tokenbased.Token;
import io.robe.auth.tokenbased.filter.TokenBasedAuthResponseFilter;
import io.robe.common.exception.RobeRuntimeException;
import io.robe.mail.MailItem;
import io.robe.mail.MailManager;
import org.hibernate.FlushMode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * Authentication Resource to provide standard Authentication services like login,change password....
 */
@Path("authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AuthResource extends AbstractAuthResource<User> {

    public static final String E_MAIL = "E-MAIL";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
    UserDao userDao;
    @Inject
    MailTemplateDao mailTemplateDao;

    @Inject
    TicketDao ticketDao;

    @Inject
    ActionLogDao actionLogDao;

    @Inject
    public AuthResource(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }


    @POST
    @UnitOfWork(flushMode = FlushMode.ALWAYS)
    @Path("login")
    @Timed
    public Response login(@Context HttpServletRequest request, Map<String, String> credentials) throws Exception {

        Optional<User> user = userDao.findByUsername(credentials.get("username"));
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else if (user.get().getPassword().equals(credentials.get("password"))) {
            if (!user.get().isActive())
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User blocked.").build();
            Map<String, String> attributes = new HashMap<>();
            attributes.put("userAgent", request.getHeader("User-Agent"));
            attributes.put("remoteAddr", request.getRemoteAddr());

            Token token = TokenManager.getInstance().createToken(user.get().getUserId(), user.get().getEmail(), DateTime.now(), attributes);
            token.setExpiration(token.getMaxAge());
            credentials.remove("password");
            credentials.put("domain", TokenBasedAuthResponseFilter.getTokenSentence("dummy"));

            user.get().setLastLoginTime(DateTime.now().toDate());
            user.get().setFailCount(0);

            logAction(new ActionLog("LOGIN", null, user.get().toString(), true));

            return Response.ok().header("Set-Cookie", TokenBasedAuthResponseFilter.getTokenSentence(token.getTokenString())).entity(credentials).build();
        } else {
            if (!user.get().isActive()) {
                logAction(new ActionLog("LOGIN", "Blocked", user.get().toString(), false));
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User blocked.").build();
            }
            int failCount = user.get().getFailCount() + 1;
            user.get().setFailCount(failCount);
            boolean block = failCount >= Integer.valueOf((String) SystemParameterCache.get("USER_BLOCK_FAIL_LIMIT", "3"));
            if (block)
                user.get().setActive(false);

            userDao.update(user.get());
            logAction(new ActionLog("LOGIN", "Wrong Password", user.get().toString(), false));

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void logAction(ActionLog login) {
        login.setOid(null);
        actionLogDao.create(login);
    }

    @POST
    @UnitOfWork
    @Path("logout")
    @Timed
    public User logout(@Auth Credentials credentials) throws Exception {
        Optional<User> user = userDao.findByUsername(credentials.getUsername());
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            BasicToken.clearPermissionCache(credentials.getUsername());
            user.get().setLastLogoutTime(DateTime.now().toDate());
            return user.get();
        }
    }

    @POST
    @UnitOfWork
    @Path("changepassword")
    public Response changePassword(@Auth Credentials clientDetails, @FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword, @FormParam("newPassword2") String newPassword2) {
        User user = getUser(clientDetails.getUsername());
        try {
            changePassword(user, oldPassword, newPassword, newPassword2);
        } catch (AuthenticationException e) {
            LOGGER.error("AuthenticationException:", e);
            return Response.serverError().entity("exception:" + e.getMessage()).build();
        }

        return Response.ok().build();
    }

    @POST
    @UnitOfWork
    @Path("forgotpassword/{forgotEmail}")
    public Response forgotPassword(@PathParam("forgotEmail") String forgotEmail, @Context UriInfo uriInfo) {

        Optional<User> userOptional = userDao.findByUsername(forgotEmail);

        if (!userOptional.isPresent()) {
            throw new RobeRuntimeException("ERROR", "Your e-mail address was not found in the system");
        }

        Optional<Ticket> ticketOptional = ticketDao.findByUserAndExpirationDate(userOptional.get());

        if (ticketOptional.isPresent()) {
            throw new RobeRuntimeException("ERROR", "Already opened your behalf tickets available");
        }

        if (!MailManager.hasConfiguration()) {
            throw new RobeRuntimeException(E_MAIL, "You do not have mail configuration.");
        }

        Ticket ticket = new Ticket();
        ticket.setType(Ticket.Type.CHANGE_PASSWORD);
        ticket.setUser(userOptional.get());
        DateTime expire = DateTime.now().plusDays(5);
        ticket.setExpirationDate(expire.toDate());
        ticket = ticketDao.create(ticket);
        String url = uriInfo.getBaseUri().toString();
        String ticketUrl = url + "ticket/" + ticket.getOid();

        MailItem mailItem = new MailItem();

        Optional<MailTemplate> mailTemplateOptional = mailTemplateDao.findByCode(Ticket.Type.CHANGE_PASSWORD.name());
        Map<String, Object> parameter = new HashMap<String, Object>();
        Writer out = new StringWriter();

        TemplateManager templateManager;

        if (mailTemplateOptional.isPresent()) {
            String body = mailTemplateOptional.get().getTemplate();

            templateManager = new TemplateManager("robeTemplate", body);
            parameter.put("name", userOptional.get().getName());
            parameter.put("surname", userOptional.get().getSurname());


        } else {
            templateManager = new TemplateManager("ChangePasswordMail.ftl");

        }
        parameter.put("ticketUrl", ticketUrl);
        templateManager.setParameter(parameter);

        templateManager.process(out);

        mailItem.setBody(out.toString());
        mailItem.setReceivers(userOptional.get().getUsername());
        mailItem.setTitle("Robe.io Password Change Request");
        MailManager.sendMail(mailItem);

        return Response.ok().build();
    }
}
