package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.auth.Credentials;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("quartzjobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuartzJobResource {

    @Inject
    private QuartzJobDao quartzJobDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<JobEntity> getAll(@Auth Credentials credentials) {
        return quartzJobDao.findAll(JobEntity.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public JobEntity get(@Auth Credentials credentials, @PathParam("id") String id) {
        JobEntity entity = quartzJobDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public JobEntity create(@Auth Credentials credentials, @Valid JobEntity model) {
        return quartzJobDao.create(model);
    }

    @PUT
    @UnitOfWork
    @Path("{id}")
    public JobEntity update(@Auth Credentials credentials, @PathParam("id") String id, @Valid JobEntity model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        JobEntity entity = quartzJobDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return quartzJobDao.update(model);
    }

    @PATCH
    @UnitOfWork
    @Path("{id}")
    public JobEntity merge(@Auth Credentials credentials, @PathParam("id") String id, JobEntity model) {
        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        JobEntity dest = quartzJobDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return quartzJobDao.update(model);
    }

    @DELETE
    @UnitOfWork
    @Path("{id}")
    public JobEntity delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid JobEntity model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        JobEntity entity = quartzJobDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return quartzJobDao.delete(entity);
    }
}
