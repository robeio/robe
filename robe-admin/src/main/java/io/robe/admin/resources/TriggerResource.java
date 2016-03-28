package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import io.robe.quartz.JobManager;
import io.robe.quartz.common.JobProvider;
import org.hibernate.FlushMode;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.hibernate.CacheMode.GET;


@Path("triggers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {

    @Inject
    private QuartzTriggerDao quartzTriggerDao;

    @Inject
    private QuartzJobDao quartzJobDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<TriggerEntity> getAll(@Auth Credentials credentials) {
        return quartzTriggerDao.findAll(TriggerEntity.class);
    }

    @POST
    @UnitOfWork
    public TriggerEntity create(TriggerEntity triggerEntity) {

        JobEntity jobEntity = quartzJobDao.findById(triggerEntity.getJobId());

        TriggerEntity entity = new TriggerEntity();

        entity.setCron(triggerEntity.getCron());
        entity.setName(triggerEntity.getName());
        entity.setGroup(triggerEntity.getGroup());
        entity.setType(triggerEntity.getType());
        //TODO set default value -1 change another good way

        entity.setStartTime(triggerEntity.getStartTime() == 0 ? -1 : triggerEntity.getStartTime());
        entity.setEndTime(triggerEntity.getEndTime() == 0 ? -1 : triggerEntity.getStartTime());

        entity.setJob(jobEntity);

        entity = quartzTriggerDao.create(entity);

        quartzTriggerDao.flush();

        jobEntity.getTriggers().add(entity);
        quartzJobDao.update(jobEntity);
        return entity;

    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public TriggerEntity update(@Auth Credentials credentials, @PathParam("id") String id, TriggerEntity triggerEntity) {

        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());

        try {
            JobManager.getInstance().unScheduleJob(entity.getName(), entity.getGroup());
            entity.setName(triggerEntity.getName());
            entity.setCron(triggerEntity.getCron());
            entity.setStartTime(triggerEntity.getStartTime());
            entity.setEndTime(triggerEntity.getEndTime());
            entity.setGroup(triggerEntity.getGroup());
            entity.setType(triggerEntity.getType());
            entity.setRepeatCount(triggerEntity.getRepeatCount());
            entity.setRepeatInterval(triggerEntity.getRepeatInterval());
            entity = quartzTriggerDao.update(entity);

            JobDetail jobDetail = JobProvider.convert2JobDetail(entity.getJob());
            Trigger trigger = JobProvider.convert2Trigger(triggerEntity);

            JobManager.getInstance().scheduleJob(jobDetail, trigger);


        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return quartzTriggerDao.update(entity);
    }


    @Path("{id}")
    @DELETE
    @UnitOfWork
    public TriggerEntity delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid TriggerEntity triggerEntity) {
        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());

        try {
            JobManager.getInstance().unScheduleJob(entity.getName(), entity.getGroup());
        } catch (SchedulerException e) {
            throw new RobeRuntimeException("ERROR", e.getLocalizedMessage());
        }
        return quartzTriggerDao.delete(entity);
    }


    @PUT
    @Path("run")
    @UnitOfWork
    public TriggerEntity fire(@Auth Credentials credentials, TriggerEntity triggerEntity) {

        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());
        JobDetail jobDetail = JobProvider.convert2JobDetail(entity.getJob());
        Trigger trigger = JobProvider.convert2Trigger(triggerEntity);
        try {
            if (!JobManager.getInstance().checkExists(entity.getName(), entity.getGroup())) {
                JobManager.getInstance().resumeTrigger(trigger.getKey());
            } else {
                JobManager.getInstance().scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            throw new RobeRuntimeException("ERROR", e.getLocalizedMessage());
        }
        entity.setActive(true);

        return quartzTriggerDao.update(entity);
    }

    @PUT
    @Path("stop")
    @UnitOfWork
    public TriggerEntity stop(@Auth Credentials credentials, TriggerEntity triggerEntity) {

        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());
        Trigger trigger = JobProvider.convert2Trigger(triggerEntity);
        try {
            JobManager.getInstance().pauseTrigger(trigger.getKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        entity.setActive(false);

        return quartzTriggerDao.update(entity);

    }


}
