package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.dto.JobInfoDTO;
import io.robe.admin.dto.TriggerInfoDTO;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import io.robe.admin.quartz.HibernateJobInfoProvider;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.quartz.JobManager;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.TriggerInfo;
import org.hibernate.FlushMode;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

import static org.hibernate.CacheMode.GET;

@Path("quartzjobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuartzJobResource {


    @Inject
    private QuartzTriggerDao triggerDao;

    @Inject
    private QuartzJobDao jobDao;

    /**
     * Return all HibernateJobInfo as a collection
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateJobInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateJobInfo as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Collection<JobInfoDTO> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {
        List<JobInfoDTO> dtoList = new LinkedList<>();
        for (HibernateJobInfo info : jobDao.findAllStrict(search)) {
            JobInfoDTO dto = new JobInfoDTO(info);
            try {
                if (!JobManager.getInstance().isScheduledJob(dto.getName(), dto.getGroup())) {
                    dto.setStatus(JobInfoDTO.Status.UNSCHEDULED);
                } else {
                    if (JobManager.getInstance().isPausedJob(dto.getName(), dto.getGroup())) {
                        dto.setStatus(JobInfoDTO.Status.PAUSED);
                    } else {
                        dto.setStatus(JobInfoDTO.Status.ACTIVE);
                    }
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * Return a HibernateJobInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateJobInfo}
     * @return a  {@link HibernateJobInfo} resource with the matches given id.
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns a HibernateJobInfo resource with the matches given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public HibernateJobInfo get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateJobInfo entity = jobDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }


    /**
     * Returns all HibernateTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateTriggerInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateTriggerInfo as a collection with the matches given job id.")
    @GET
    @Path("{id}/triggers")
    @UnitOfWork
    public List<TriggerInfoDTO> getJobTriggers(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        List<TriggerInfoDTO> dtos = new LinkedList<>();
        for (HibernateTriggerInfo info : triggerDao.findByJobOid(id)) {
            TriggerInfoDTO dto = new TriggerInfoDTO(info);
            try {
                if (!JobManager.getInstance().isScheduledTrigger(dto.getName(), dto.getGroup())) {
                    dto.setStatus(JobInfoDTO.Status.UNSCHEDULED);
                } else {
                    if (JobManager.getInstance().isPausedTrigger(dto.getName(), dto.getGroup())) {
                        dto.setStatus(JobInfoDTO.Status.PAUSED);
                    } else {
                        dto.setStatus(JobInfoDTO.Status.ACTIVE);
                    }
                }
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * Returns all HibernateTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateTriggerInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateTriggerInfo as a collection with the matches given job id.")
    @PUT
    @Path("{id}/schedule")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public boolean schedule(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateJobInfo info = jobDao.findById(id);
        if (info == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        try {
            JobInfo dto = new HibernateJobInfoProvider().getJob(info.getJobClass());
            JobDetail detail = HibernateJobInfoProvider.convert2JobDetail(dto);
            Set<Trigger> triggers = new HashSet<>(dto.getTriggers().size());
            for (TriggerInfo triggerInfo : dto.getTriggers()) {
                if (triggerInfo.getType().equals(TriggerInfo.Type.CRON) ||
                        triggerInfo.getType().equals(TriggerInfo.Type.SIMPLE)) {
                    triggers.add(HibernateJobInfoProvider.convert2Trigger(triggerInfo, dto));
                }
            }
            JobManager.getInstance().scheduleJob(detail, triggers, false);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
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
        HibernateJobInfo info = jobDao.findById(id);
        if (info == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        try {
            return JobManager.getInstance().unScheduleJob(info.getName(), info.getGroup());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
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
        HibernateJobInfo info = jobDao.findById(id);
        if (info == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        try {
            JobManager.getInstance().pauseJob(info.getName(), info.getGroup());
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
        HibernateJobInfo info = jobDao.findById(id);
        if (info == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        try {
            JobManager.getInstance().resumeJob(info.getName(), info.getGroup());
        } catch (SchedulerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
