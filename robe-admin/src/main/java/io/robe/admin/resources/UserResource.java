package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.User;
import io.robe.auth.AbstractAuthResource;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
import io.robe.common.utils.FieldReflection;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends AbstractAuthResource<User> {

    private UserDao userDao;

    @Inject
    public UserResource(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    /**
     * Returns all User as a collection with the related path.
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @return Returns all @{@link User} as a collection with the related path.
     */
    @RobeService(group = "User", description = "Returns all User as a collection with the related path.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public List<User> getAll(@Auth Credentials credentials) {
        return userDao.findAll(User.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public User get(@Auth Credentials credentials, @PathParam("id") String id) {
        User entity = userDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public User create(@Auth Credentials credentials, @Valid User model) {
        return userDao.create(model);
    }

    @PUT
    @UnitOfWork
    @Path("{id}")
    public User update(@Auth Credentials credentials, @PathParam("id") String id, @Valid User model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        User entity = userDao.findById(id);
        userDao.detach(entity);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return userDao.update(model);
    }

    @PATCH
    @UnitOfWork
    @Path("{id}")
    public User merge(@Auth Credentials credentials, @PathParam("id") String id, User model) {
        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        User dest = userDao.findById(id);
        userDao.detach(dest);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return userDao.update(model);
    }

    @DELETE
    @UnitOfWork
    @Path("{id}")
    public User delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid User model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        User entity = userDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return userDao.delete(entity);
    }


}
