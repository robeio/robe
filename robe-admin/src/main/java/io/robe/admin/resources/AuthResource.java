package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.metrics.annotation.Timed;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.User;
import io.robe.auth.AbstractAuthResource;
import io.robe.auth.Credentials;
import io.robe.auth.IsToken;
import io.robe.auth.TokenWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;


/**
 * Authentication Resource to provide standard Authentication services like login,change password....
 */
@Path("authentication")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AuthResource extends  AbstractAuthResource<User>{

    UserDao userDao;

    @Inject
    public AuthResource(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }


    @POST
    @UnitOfWork
    @Timed
    @Path("login")
    public Response login(@Context HttpServletRequest request, Map<String, String> credentials) throws Exception {

        Optional<User> user = userDao.findByUsername(credentials.get("username"));
        if (!user.isPresent()) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else if (user.get().getPassword().equals(credentials.get("password"))) {
            IsToken token = TokenWrapper.createToken(user.get().getEmail(), null);
            credentials.remove("password");
            return Response.ok().header("Set-Cookie", "auth-token" + "=" + token.getToken() + ";path=/;domain=" + request.getRemoteHost() + ";").entity(credentials).build();

        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

    }

    @POST
    @UnitOfWork
    @Path("changepassword")
    public Response changePassword(@Auth Credentials clientDetails, @FormParam("oldPassword") String oldPassword, @FormParam("newPassword") String newPassword,@FormParam("newPassword2") String newPassword2) {
        Optional<User> user = userDao.findByUsername(clientDetails.getUsername());
        try {
            super.changePassword(user.get(), oldPassword, newPassword, newPassword2);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }

        return Response.ok().build();
    }


    @POST
    @UnitOfWork
    @Path("forgotpassword")
    public User forgotPassword(@FormParam("email") String email, @FormParam("clientId") String clientId, @Context UriInfo uriInfo) {
        //TODO password request
        return null;
    }

    public User completeforgotPassword(String email, String clientId) {
        //TODO complete request with a mail
        return null;
    }

}
