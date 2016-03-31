package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import io.robe.auth.Credentials;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;


@Path("triggers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {

    @Inject
    private QuartzTriggerDao quartzTriggerDao;


    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<TriggerEntity> getAll(@Auth Credentials credentials) {
        return quartzTriggerDao.findAll(TriggerEntity.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public TriggerEntity get(@Auth Credentials credentials, @PathParam("id") String id) {
        TriggerEntity entity = quartzTriggerDao.findById(JobEntity.class, id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public TriggerEntity create(@Auth Credentials credentials, @Valid TriggerEntity model) {
        return quartzTriggerDao.create(model);
    }

    @PUT
    @UnitOfWork
    @Path("{id}")
    public TriggerEntity update(@Auth Credentials credentials, @PathParam("id") String id, @Valid TriggerEntity model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }

        return quartzTriggerDao.update(model);
    }


    @DELETE
    @UnitOfWork
    @Path("{id}")
    public TriggerEntity delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid TriggerEntity model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        TriggerEntity entity = quartzTriggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return quartzTriggerDao.delete(entity);
    }

}
