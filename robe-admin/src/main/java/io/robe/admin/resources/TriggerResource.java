package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
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
    private QuartzJobDao jobDao;
    @Inject
    private QuartzTriggerDao triggerDao;


    /**
     * Create a {@link HibernateTriggerInfo} resource.
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param model       This is the one model of {@link HibernateTriggerInfo}
     * @return create a {@link HibernateTriggerInfo} resource.
     */
    @RobeService(group = "QuartzJob", description = "Create a HibernateTriggerInfo resource.")
    @POST
    @UnitOfWork
    public HibernateTriggerInfo create(@RobeAuth Credentials credentials, @Valid HibernateTriggerInfo model) {
        return triggerDao.create(model);
    }

    /**
     * Update a HibernateTriggerInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateTriggerInfo}
     * @param model       This is the one model of {@link HibernateTriggerInfo}
     * @return Update a  {@link HibernateTriggerInfo} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Update a HibernateTriggerInfo resource with the matches given id.")
    @PUT
    @UnitOfWork
    @Path("{id}")
    public HibernateTriggerInfo update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HibernateTriggerInfo model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        HibernateTriggerInfo entity = triggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        triggerDao.detach(entity);
        return triggerDao.update(model);
    }

    /**
     * Delete a HibernateTriggerInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateTriggerInfo}
     * @param model       This is the one model of {@link HibernateTriggerInfo}
     * @return Delete a  {@link HibernateTriggerInfo} resource  with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Delete a HibernateTriggerInfo resource with the matches given id.")
    @DELETE
    @UnitOfWork
    @Path("{id}")
    public HibernateTriggerInfo delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HibernateTriggerInfo model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        HibernateTriggerInfo entity = triggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return triggerDao.delete(entity);
    }

    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/schedule")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public boolean schedule(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateTriggerInfo info = triggerDao.findById(id);
        HibernateJobInfo jobEntity = jobDao.findById(info.getJobOid());
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
     * Returns all HibernateTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateTriggerInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/unschedule")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public boolean unschedule(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateTriggerInfo info = triggerDao.findById(id);
        HibernateJobInfo jobEntity = jobDao.findById(info.getJobOid());

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
     * Returns all HibernateTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateTriggerInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/pause")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public boolean pause(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateTriggerInfo info = triggerDao.findById(id);
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
     * Returns all HibernateTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateTriggerInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/resume")
    @UnitOfWork
    public boolean resume(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateTriggerInfo info = triggerDao.findById(id);
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
