package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.entity.Role;
import io.robe.auth.Credentials;
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

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Role> getAll(@Auth Credentials credentials) {
        return roleDao.findAll(Role.class);
    }

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

    @POST
    @UnitOfWork
    public Role create(@Auth Credentials credentials, @Valid Role model) {
        return roleDao.create(model);
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public Role update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Role model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Role entity = roleDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return roleDao.update(model);
    }

    @Path("{id}")
    @PATCH
    @UnitOfWork
    public Role merge(@Auth Credentials credentials, @PathParam("id") String id, Role model) {

        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Role dest = roleDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return roleDao.update(model);
    }

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
