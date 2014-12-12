package io.robe.admin.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.MailTemplateDao;
import io.robe.admin.hibernate.dao.TicketDao;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.admin.hibernate.entity.User;
import io.robe.auth.AbstractAuthResource;
import io.robe.auth.Credentials;
import io.robe.auth.tokenbased.Token;
import io.robe.auth.tokenbased.TokenFactory;
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
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.CacheMode.GET;


/**
 * Authentication Resource to provide standard Authentication services like login,change password....
 */
@Path("authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AuthResource extends AbstractAuthResource<User> {

    public static final String E_MAIL = "E-MAIL";
    public static final String TEMPLATES_PATH = "/templates/";
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);
    UserDao userDao;
    @Inject
    MailTemplateDao mailTemplateDao;

    @Inject
    TicketDao ticketDao;

    @Inject
    public AuthResource(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }


    @POST
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    @Path("login")
    @Timed
    public Response login(@Context HttpServletRequest request, Map<String, String> credentials) throws Exception {


        Optional<User> user = userDao.findByUsername(credentials.get("username"));
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else if (user.get().getPassword().equals(credentials.get("password"))) {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("userAgent", request.getHeader("User-Agent"));
            attributes.put("remoteAddr", request.getRemoteAddr());

            Token token = TokenFactory.getInstance().createToken(user.get().getEmail(), DateTime.now(), attributes);
            token.setExpiration(token.getMaxAge());
            credentials.remove("password");

            return Response.ok().header("Set-Cookie", TokenBasedAuthResponseFilter.getTokenSentence(token.getTokenString())).entity(credentials).build();
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
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

        Optional<Ticket> ticketOptional = ticketDao.findByUserAndActive(userOptional.get());

        if (ticketOptional.isPresent()) {
            throw new RobeRuntimeException("ERROR", "Already opened your behalf tickets available");
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
        Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        Template template = null;
        Map<String, Object> parameter = new HashMap<String, Object>();
        Writer out = new StringWriter();

        if (mailTemplateOptional.isPresent()) {
            String body = mailTemplateOptional.get().getTemplate();
            try {
                template = new Template("robeTemplate", body, cfg);
                parameter.put("ticketUrl", ticketUrl);
                parameter.put("name", userOptional.get().getName());
                parameter.put("surname", userOptional.get().getSurname());
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            cfg.setClassForTemplateLoading(this.getClass(), TEMPLATES_PATH);
            parameter.put("ticketUrl", ticketUrl);
            try {
                template = cfg.getTemplate("ChangePasswordMail.ftl");
            } catch (IOException e) {
                throw new RobeRuntimeException(E_MAIL, "ChangePasswordMail template not found:" + e.getLocalizedMessage());
            }

        }

        try {
            template.process(parameter, out);
        } catch (TemplateException e) {
            throw new RobeRuntimeException(E_MAIL, e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mailItem.setBody(out.toString());
        mailItem.setReceivers(userOptional.get().getUsername());
        mailItem.setTitle("Robe.io Password Change Request");
        MailManager.sendMail(mailItem);

        return Response.ok().build();
    }
}
