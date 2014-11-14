package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.auth.Credentials;
import io.robe.quartz.ManagedQuartz;
import io.robe.quartz.QuartzBundle;
import io.robe.quartz.QuartzJob;
import io.robe.quartz.hibernate.JobEntity;
import io.robe.quartz.hibernate.TriggerEntity;
import org.quartz.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.quartz.TriggerBuilder.newTrigger;


@Path("trigger")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {

    @Inject
    QuartzTriggerDao quartzTriggerDao;
    @Inject
    ManagedQuartz managedQuartz;

    @Inject
    QuartzJobDao quartzJobDao;

    @GET
    @UnitOfWork
    public List<TriggerEntity> getAll(@Auth Credentials credentials) {
        return quartzTriggerDao.findAll(TriggerEntity.class);
    }

    @DELETE
    @UnitOfWork
    public TriggerEntity delete(@Auth Credentials credentials, TriggerEntity triggerEntity) throws SchedulerException {
        triggerEntity = quartzTriggerDao.findById(triggerEntity.getOid());
        unScheduleJobTrigger(triggerEntity);
        return quartzTriggerDao.delete(triggerEntity);
    }

    @POST
    @Path("/update")
    @UnitOfWork
    public TriggerEntity setCron(TriggerEntity triggerEntity) {
        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());
        entity.setActive(triggerEntity.isActive());
        entity.setCronExpression(triggerEntity.getCronExpression());
        entity.setFireTime(triggerEntity.getFireTime());
        quartzTriggerDao.update(entity);
        return entity;
    }


    @POST
    @Path("/run")
    @UnitOfWork
    public TriggerEntity fireTrigger(TriggerEntity triggerEntity) throws SchedulerException {
        triggerEntity = quartzTriggerDao.findById(triggerEntity.getOid());
        return scheduleJob(triggerEntity);
    }

    @POST
    @Path("/stop")
    @UnitOfWork
    public TriggerEntity stopTrigger(TriggerEntity triggerEntity) throws SchedulerException {
        triggerEntity = quartzTriggerDao.findById(triggerEntity.getOid());
        return unScheduleJobTrigger(triggerEntity);

    }

    @POST
    @Path("/add")
    @UnitOfWork
    public TriggerEntity addTrigger(JobEntity quartzJob) throws SchedulerException {

        TriggerEntity entity = new TriggerEntity();
        entity.setJob(quartzJob);
        entity = quartzTriggerDao.create(entity);
        quartzTriggerDao.flush();
        quartzJob.getTriggers().add(entity);
        quartzJobDao.update(quartzJob);
        return entity;

    }

    /**
     * Creates a new TriggerEntity by using parent quartzJob Id
     *
     * @param quartzJob
     * @return
     */
    private TriggerEntity createQuartzTrigger(JobEntity quartzJob) {
        TriggerEntity newTriggerEntity = new TriggerEntity();
        newTriggerEntity.setJob(quartzJob);
        quartzTriggerDao.create(newTriggerEntity);
        return newTriggerEntity;
    }

    /**
     * Unschedule specified trigger
     *
     * @param triggerEntity
     * @return
     * @throws SchedulerException
     */
    public TriggerEntity unScheduleJobTrigger(TriggerEntity triggerEntity) throws SchedulerException {
        Scheduler scheduler = managedQuartz.getScheduler();
        QuartzJob quartzJob = triggerEntity.getJob();
        scheduler.pauseTrigger(TriggerKey.triggerKey(triggerEntity.getOid(), quartzJob.getOid()));
        triggerEntity.setActive(false);
        quartzTriggerDao.update(triggerEntity);

        return triggerEntity;
    }

    /**
     * Gets TriggerEntity and fire with its job class
     *
     * @param triggerEntity
     * @return fired triggerEntity
     * @throws SchedulerException
     */
    public TriggerEntity scheduleJob(TriggerEntity triggerEntity) throws SchedulerException {
        String cronExpression = triggerEntity.getCronExpression();
        QuartzJob quartzJob = triggerEntity.getJob();
        Scheduler scheduler = managedQuartz.getScheduler();
        JobDetail jobDetail = scheduler.getJobDetail(new JobKey(quartzJob.getOid(), QuartzBundle.STATIC_GROUP));
        if (cronExpression != null) {
            TriggerKey triggerKey = new TriggerKey(triggerEntity.getOid(), quartzJob.getOid());
            TriggerBuilder<Trigger> trigger = newTrigger().startAt(new Date(triggerEntity.getFireTime())).withIdentity(triggerKey);
            Set<Trigger> triggers = new LinkedHashSet<Trigger>();
            triggers.add(trigger.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build());
            scheduler.scheduleJob(jobDetail, triggers, true);
            triggerEntity.setActive(true);
            triggerEntity = quartzTriggerDao.update(triggerEntity);
        }
        return triggerEntity;
    }


}
