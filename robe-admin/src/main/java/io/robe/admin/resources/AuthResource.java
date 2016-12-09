package io.robe.admin.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.ActionLogDao;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.ActionLog;
import io.robe.admin.hibernate.entity.User;
import io.robe.admin.util.SystemParameterCache;
import io.robe.auth.AbstractAuthResource;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.auth.token.BasicToken;
import io.robe.auth.token.jersey.TokenBasedAuthResponseFilter;
import org.hibernate.FlushMode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.CacheMode.GET;


/**
 * Authentication Resource to provide standard Authentication services like login,change password....
 */
@Path("authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AuthResource extends AbstractAuthResource<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

    private UserDao userDao;

    @Inject
    private ActionLogDao actionLogDao;

    @Inject
    public AuthResource(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }


    /**
     * check username and password for authentication
     * <p>
     * Status Code:
     * UNAUTHORIZED no have authentication.
     * INTERNAL_SERVER_ERROR Blocked.
     *
     * @param request     HttpServletRequest
     * @param credentials username and password (SHA256)
     * @return {@link Response}
     * @throws Exception for createToken
     */

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


            BasicToken token = new BasicToken(user.get().getUserId(), user.get().getEmail(), DateTime.now(), attributes);
            token.setExpiration(token.getMaxAge());
            credentials.remove("password");
            credentials.put("domain", TokenBasedAuthResponseFilter.getTokenSentence("dummy"));

            user.get().setLastLoginTime(DateTime.now().toDate());
            user.get().setFailCount(0);

            logAction(new ActionLog("LOGIN", null, user.get().toString(), true, request.getRemoteAddr()));

            return Response.ok().header("Set-Cookie", TokenBasedAuthResponseFilter.getTokenSentence(token.getTokenString())).entity(credentials).build();
        } else {
            if (!user.get().isActive()) {
                logAction(new ActionLog("LOGIN", "Blocked", user.get().toString(), false, request.getRemoteAddr()));
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User blocked.").build();
            }
            int failCount = user.get().getFailCount() + 1;
            user.get().setFailCount(failCount);
            boolean block = failCount >= Integer.valueOf((String) SystemParameterCache.get("USER_BLOCK_FAIL_LIMIT", "3"));
            if (block)
                user.get().setActive(false);

            userDao.update(user.get());
            logAction(new ActionLog("LOGIN", "Wrong Password", user.get().toString(), false, request.getRemoteAddr()));

            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void logAction(ActionLog login) {
        login.setOid(null);
        actionLogDao.create(login);
    }

    /**
     * Log out
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @return {@link User} when logout
     * @throws Exception for clear permission cache
     */

    @POST
    @UnitOfWork
    @Path("logout")
    @Timed
    public Response logout(@RobeAuth Credentials credentials, @Context HttpServletRequest request) throws Exception {
        Optional<User> user = userDao.findByUsername(credentials.getUsername());
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            BasicToken.clearPermissionCache(credentials.getUsername());
            user.get().setLastLogoutTime(DateTime.now().toDate());
            logAction(new ActionLog("LOGOUT", null, user.get().toString(), true, request.getRemoteAddr()));
            return Response.ok().header("Set-Cookie", TokenBasedAuthResponseFilter.getTokenSentence("")).build();
        }
    }


    /**
     * User Information Returns
     * <p>
     * Status Code:
     * UNAUTHORIZED no have authentication.
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @return {@link User}
     * @throws Exception for not found user
     */
    @Path("profile")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public User getProfile(@RobeAuth Credentials credentials) {
        Optional<User> user = userDao.findByUsername(credentials.getUsername());
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else
            return user.get();
    }

    /**
     * User Change Password
     * <p>
     * Status Code:
     * NOT_FOUND not found user.
     * PRECONDITION_FAILED change passsword error.
     *
     * @param request     HttpServletRequest
     * @param passwords   password(SHA256),newpassword(SHA256) and newpasswordrpt(SHA256)
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @return {@link Response}
     * @throws Exception for not found user
     */
    @POST
    @UnitOfWork
    @Path("password")
    @Timed
    public Response changePassword(@Context HttpServletRequest request, @RobeAuth Credentials credentials, Map<String, String> passwords) {

        Optional<User> user = userDao.findByUsername(credentials.getUsername());
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else if (user.get().getPassword().equals(passwords.get("password"))) {
            if (passwords.get("newPassword").equals(passwords.get("newPasswordRepeat"))) {
                user.get().setPassword(passwords.get("newPassword"));
                return Response.status(Response.Status.OK).entity("Your password has been updated").build();
            } else {
                return Response.status(Response.Status.PRECONDITION_FAILED).entity("Your new password does not match.").build();
            }

        } else {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("Your password is incorrect.").build();
        }

    }

}
