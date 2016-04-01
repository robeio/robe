package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.entity.Role;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("roles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    @Inject
    private RoleDao roleDao;

    /**
     * Returns all Role as a collection with the related path.
     *
     * @param credentials auto fill by {@link Auth} annotation for authentication.
     * @return all {@link Role} as a collection
     */
    @RobeService(group = "Role", description = "Returns all Role as a collection with the related path.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Role> getAll(@Auth Credentials credentials) {
        return roleDao.findAll(Role.class);
    }

    /**
     * Return a single Role related with the path and matches with the given id.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @return a single {@link Role} related with the path and matches with the given id.
     */
    @RobeService(group = "Role", description = "Returns a single Role related with the path and matches with the given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Role get(@Auth Credentials credentials, @PathParam("id") String id) {
        Role entity = roleDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Creates a single  related with the path and returns given Role path link at header Location=example/{id]
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param model       This is the one model of {@link Role}
     * @return Creates a single  related with the path and returns given {@link Role} path link at header Location=example/{id]
     */
    @RobeService(group = "Role", description = "Creates a single  related with the path and returns given Role path link at header Location=example/{id]")
    @POST
    @UnitOfWork
    public Role create(@Auth Credentials credentials, @Valid Role model) {
        return roleDao.create(model);
    }

    /**
     * Updates a single Role related with the path and matches with the given id. Payload holds the whole data.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @param model       This is the one model of {@link Role}
     * @return Updates a single {@link Role} related with the path and matches with the given id.
     */
    @RobeService(group = "Role", description = "Updates a single Role related with the path and matches with the given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public Role update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Role entity = roleDao.findById(id);
        roleDao.detach(entity);

        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return roleDao.update(model);
    }

    /**
     * Updates a single Role related with the path and matches with the given id. Payload will only containe update data . Version of the Role can be available at ETag in an If-Match header.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @param model       This is the one model of {@link Role}
     * @return Updates a single {@link Role} related with the path and matches with the given id.
     */
    @RobeService(group = "Role", description = "Updates a single Role related with the path and matches with the given id.")
    @Path("{id}")
    @PATCH
    @UnitOfWork
    public Role merge(@Auth Credentials credentials, @PathParam("id") String id, Role model) {

        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Role dest = roleDao.findById(id);
        roleDao.detach(dest);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return roleDao.update(model);
    }

    /**
     * Deletes a single Role for the related path and returns given Role path link at header Location=example/{id].
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Role}
     * @param model       This is the one model of {@link Role}
     * @return Deletes a single {@link Role} for the related path.
     */
    @RobeService(group = "Role", description = "Deletes a single Role for the related path.")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Role delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Role entity = roleDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return roleDao.delete(entity);
    }

}
