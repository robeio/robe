package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.PermissionDao;
import io.robe.admin.hibernate.entity.Permission;
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

@Path("permissions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PermissionResource {

    @Inject
    private PermissionDao permissionDao;

    /**
     * Return all {@link Permission}s as a collection.
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @return all @{@link Permission}s as a collection.
     */
    @RobeService(group = "Permission", description = "Return all permissions as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Permission> getAll(@Auth Credentials credentials) {
        return permissionDao.findAll(Permission.class);
    }

    /**
     * Returns a {@link Permission} resource with the given id
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @return a @{@link Permission} resource macthes with the given id.
     */
    @RobeService(group = "Permission", description = "Returns a permission resource with the given id")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Permission get(@Auth Credentials credentials, @PathParam("id") String id) {
        Permission entity = permissionDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Creates a {@link Permission} resource.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param model       Data of {@link Permission}
     * @return Creates a @{@link Permission} resource.
     */
    @RobeService(group = "Permission", description = "Creates a permission resource.")
    @POST
    @UnitOfWork
    public Permission create(@Auth Credentials credentials, @Valid Permission model) {
        return permissionDao.create(model);
    }

    /**
     * Updates a {@link Permission} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @param model       Data of {@link Permission}
     * @return Updates a @{@link Permission} resource matches with the given id.
     */
    @RobeService(group = "Permission", description = "Updates a permission resource matches with the given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public Permission update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Permission model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Permission entity = permissionDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return permissionDao.update(model);
    }

    /**
     * Updates a {@link Permission} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @param model       Data of {@link Permission}
     * @return Updates a @{@link Permission} resource matches with the given id.
     */
    @RobeService(group = "Permission", description = "Updates a permission resource matches with the given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public Permission merge(@Auth Credentials credentials, @PathParam("id") String id, Permission model) {
        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Permission dest = permissionDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return permissionDao.update(model);
    }

    /**
     * Deletes a {@link Permission} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Permission}
     * @param model       Data of {@link Permission}
     * @return deletes a @{@link Permission} resource matches with the given id.
     */
    @RobeService(group = "Permission", description = "Deletes a permission resource matches with the given id.")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Permission delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Permission model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Permission entity = permissionDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return permissionDao.delete(entity);
    }
}
