package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.hibernate.entity.User;
import io.robe.auth.AbstractAuthResource;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.reflection.Fields;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

import javax.inject.Inject;
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
     * Returns all {@link User}s as a collection.
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @return all {@link User}s as a collection with.
     */

    @RobeService(group = "User", description = "Returns all Users as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public List<User> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {
        return userDao.findAllStrict(search);
    }

    /**
     * Returns a single User matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is the oid of {@link User}
     * @return a {@link User} resource matches with the given id.
     */
    @RobeService(group = "User", description = "Returns a User resource matches with the given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public User get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        User entity = userDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create as a {@link User} resource.
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @param model       data of {@link User}
     * @return Create as a {@link User} resource.
     */
    @RobeService(group = "User", description = "Create as a User resource.")
    @POST
    @UnitOfWork
    public User create(@RobeAuth Credentials credentials, @Valid User model) {
        return userDao.create(model);
    }

    /**
     * Updates a single {@link User} matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is the oid of {@link User}
     * @param model       data of {@link User}
     * @return Updates a single {@link User} matches with the given id.
     */
    @RobeService(group = "User", description = "Updates a single User matches with the given id.")
    @PUT
    @UnitOfWork
    @Path("{id}")
    public User update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid User model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        User entity = userDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        userDao.detach(entity);
        return userDao.update(model);
    }

    /**
     * Updates a single {@link User} matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is the oid of {@link User}
     * @param model       data of {@link User}
     * @return Updates a single {@link User} matches with the given id.
     */
    @RobeService(group = "User", description = "Updates a single User matches with the given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public User merge(@RobeAuth Credentials credentials, @PathParam("id") String id, User model) {
        if (!id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        User dest = userDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        Fields.mergeRight(model, dest);
        return userDao.update(dest);
    }

    /**
     * Deletes a single {@link User} matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is the oid of {@link User}
     * @param model       data of {@link User}
     * @return delete a single {@link User} matches with the given id.
     */
    @RobeService(group = "User", description = "Deletes a single User matches with the given id.")
    @DELETE
    @UnitOfWork
    @Path("{id}")
    public User delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid User model) {
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
