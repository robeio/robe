package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.dto.TriggerInfoDTO;
import io.robe.admin.hibernate.dao.JobDao;
import io.robe.admin.hibernate.dao.TriggerDao;
import io.robe.admin.hibernate.entity.HJobInfo;
import io.robe.admin.hibernate.entity.HTriggerInfo;
import io.robe.admin.quartz.HibernateJobInfoProvider;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.common.service.RobeService;
import io.robe.quartz.JobManager;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.TriggerInfo;
import org.hibernate.FlushMode;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hibernate.CacheMode.GET;


@Path("triggers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {

    @Inject
    private JobDao jobDao;
    @Inject
    private TriggerDao triggerDao;


    /**
     * Create a {@link HTriggerInfo} resource.
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param model       This is the one model of {@link HTriggerInfo}
     * @return create a {@link HTriggerInfo} resource.
     */
    @RobeService(group = "QuartzJob", description = "Create a HTriggerInfo resource.")
    @POST
    @UnitOfWork
    public HTriggerInfo create(@RobeAuth Credentials credentials, @Valid HTriggerInfo model) {
        //Save to the DB
        HJobInfo job = jobDao.findById(model.getJobOid());
        if (job == null) {
            throw new WebApplicationException("Job not found", Response.Status.NOT_FOUND);
        }
        if (!job.getProvider().equals(HibernateJobInfoProvider.class)) {
            throw new WebApplicationException("Trigger is not provided by an editable source.", Response.Status.PRECONDITION_FAILED);
        }
        HTriggerInfo record = triggerDao.create(model);
        return new TriggerInfoDTO(record);
    }

    /**
     * Update a HTriggerInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HTriggerInfo}
     * @param model       This is the one model of {@link HTriggerInfo}
     * @return Update a  {@link HTriggerInfo} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Update a HTriggerInfo resource with the matches given id.")
    @PUT
    @UnitOfWork
    @Path("{id}")
    public HTriggerInfo update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HTriggerInfo model) {
        HJobInfo job = jobDao.findById(model.getJobOid());
        if (job == null) {
            throw new WebApplicationException("Job not found", Response.Status.NOT_FOUND);
        }
        if (!job.getProvider().equals(HibernateJobInfoProvider.class)) {
            throw new WebApplicationException("Trigger is not provided by an editable source.", Response.Status.PRECONDITION_FAILED);
        }
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException("URL trigger id is not same with the payload id", Response.Status.FORBIDDEN);
        }
        HTriggerInfo entity = triggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Trigger not found", Response.Status.NOT_FOUND);
        }
        triggerDao.detach(entity);
        model =  triggerDao.update(model);
        return new TriggerInfoDTO(model);
    }

    /**
     * Delete a HTriggerInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HTriggerInfo}
     * @param model       This is the one model of {@link HTriggerInfo}
     * @return Delete a  {@link HTriggerInfo} resource  with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Delete a HTriggerInfo resource with the matches given id.")
    @DELETE
    @UnitOfWork
    @Path("{id}")
    public HTriggerInfo delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HTriggerInfo model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        HTriggerInfo entity = triggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return triggerDao.delete(entity);
    }

    @RobeService(group = "HJobInfo", description = "Returns all HTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/schedule")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public boolean schedule(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HTriggerInfo info = triggerDao.findById(id);
        HJobInfo jobEntity = jobDao.findById(info.getJobOid());
        if (info == null || jobEntity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        JobInfo job = new HibernateJobInfoProvider().getJob(jobEntity.getJobClass());

        try {
            if (info.getType().equals(TriggerInfo.Type.CRON) ||
                    info.getType().equals(TriggerInfo.Type.SIMPLE)) {
                Trigger trigger = new HibernateJobInfoProvider().convert2Trigger(info, job);
                JobManager.getInstance().scheduleTrigger(trigger);
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns all HTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HTriggerInfo} as a collection
     */
    @RobeService(group = "HJobInfo", description = "Returns all HTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/unschedule")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public boolean unschedule(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HTriggerInfo info = triggerDao.findById(id);
        HJobInfo jobEntity = jobDao.findById(info.getJobOid());

        if (info == null || jobEntity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        JobInfo job = new HibernateJobInfoProvider().getJob(jobEntity.getJobClass());

        if (info == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        try {
            if (info.getType().equals(TriggerInfo.Type.CRON) ||
                    info.getType().equals(TriggerInfo.Type.SIMPLE)) {
                Trigger trigger = new HibernateJobInfoProvider().convert2Trigger(info, job);
                JobManager.getInstance().unScheduleTrigger(trigger);
                return true;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns all HTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HTriggerInfo} as a collection
     */
    @RobeService(group = "HJobInfo", description = "Returns all HTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/pause")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public boolean pause(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HTriggerInfo info = triggerDao.findById(id);
        if (info == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        try {
            JobManager.getInstance().pauseTrigger(TriggerKey.triggerKey(info.getName(), info.getGroup()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Returns all HTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HTriggerInfo} as a collection
     */
    @RobeService(group = "HJobInfo", description = "Returns all HTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/resume")
    @UnitOfWork
    public boolean resume(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HTriggerInfo info = triggerDao.findById(id);
        if (info == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        try {
            JobManager.getInstance().resumeTrigger(TriggerKey.triggerKey(info.getName(), info.getGroup()));
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
