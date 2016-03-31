package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.PermissionDao;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.auth.Credentials;
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

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Permission> getAll() {
        return permissionDao.findAll(Permission.class);
    }

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

    @POST
    @UnitOfWork
    public Permission create(@Auth Credentials credentials, @Valid Permission model) {
        return permissionDao.create(model);
    }

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
