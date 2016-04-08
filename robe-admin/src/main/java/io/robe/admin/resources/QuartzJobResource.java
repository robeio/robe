package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
import io.robe.common.service.SearchParam;
import io.robe.common.service.jersey.model.SearchModel;
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
    private QuartzTriggerDao quartzTriggerDao;

    @Inject
    private QuartzJobDao quartzJobDao;


    /**
     * Returns all Trigger as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link Auth} annotation for authentication.
     * @return all {@link TriggerEntity} as a collection
     */
    @RobeService(group = "JobEntity", description = "Returns all Trigger as a collection with the matches given job id.")
    @GET
    @Path("{id}/triggers")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<TriggerEntity> getJobTriggers(@Auth Credentials credentials, @PathParam("id") String id) {
        return quartzTriggerDao.findByJobOid(id);
    }


    /**
     * Return all JobEntity as a collection
     *
     * @param credentials auto fill by {@link Auth} annotation for authentication.
     * @return all {@link JobEntity} as a collection
     */
    @RobeService(group = "JobEntity", description = "Returns all JobEntity as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<JobEntity> getAll(@Auth Credentials credentials, @SearchParam SearchModel search) {
        return quartzJobDao.findAll(search);
    }

    /**
     * Return a JobEntity resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link JobEntity}
     * @return a  {@link JobEntity} resource with the matches given id.
     */
    @RobeService(group = "JobEntity", description = "Returns a JobEntity resource with the matches given id.")
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

    /**
     * Create a {@link JobEntity} resource.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param model       This is the one model of {@link JobEntity}
     * @return create a {@link JobEntity} resource.
     */
    @RobeService(group = "JobEntity", description = "Create a JobEntity resource.")
    @POST
    @UnitOfWork
    public JobEntity create(@Auth Credentials credentials, @Valid JobEntity model) {
        return quartzJobDao.create(model);
    }

    /**
     * Update a JobEntity resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link JobEntity}
     * @param model       This is the one model of {@link JobEntity}
     * @return Update a  {@link JobEntity} resource with the matches given id.
     */
    @RobeService(group = "JobEntity", description = "Update a JobEntity resource with the matches given id.")
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

    /**
     * Update a JobEntity resource with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link JobEntity}
     * @param model       This is the one model of {@link JobEntity}
     * @return Updates a  {@link JobEntity} resource with the matches given id.
     */
    @RobeService(group = "JobEntity", description = "Update a JobEntity resource with the matches given id.")
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

    /**
     * Delete a JobEntity resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link JobEntity}
     * @param model       This is the one model of {@link JobEntity}
     * @return Delete a  {@link JobEntity} resource  with the matches given id.
     */
    @RobeService(group = "JobEntity", description = "Delete a JobEntity resource with the matches given id.")
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
