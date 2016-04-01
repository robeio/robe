package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.Credentials;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceResource {


    @Inject
    private ServiceDao serviceDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Service> getAll() {
        return serviceDao.findAll(Service.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Service get(@Auth Credentials credentials, @PathParam("id") String id) {
        Service entity = serviceDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public Service create(@Auth Credentials credentials, @Valid Service model) {
        return serviceDao.create(model);
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public Service update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Service model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Service entity = serviceDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return serviceDao.update(model);
    }

    @PATCH
    @UnitOfWork
    @Path("{id}")
    public Service merge(@Auth Credentials credentials, @PathParam("id") String id, Service model) {
        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Service dest = serviceDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return serviceDao.update(model);
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Service delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Service model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Service entity = serviceDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return serviceDao.delete(entity);
    }
}
