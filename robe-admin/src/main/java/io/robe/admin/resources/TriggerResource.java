package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import io.robe.auth.Credentials;
import io.robe.quartz.common.JobInfo;
import org.hibernate.FlushMode;
import org.quartz.SchedulerException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.hibernate.CacheMode.GET;


@Path("trigger")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {

    @Inject
    QuartzTriggerDao quartzTriggerDao;
    @Inject
    QuartzJobDao quartzJobDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<TriggerEntity> getAll(@Auth Credentials credentials) {
        return quartzTriggerDao.findAll(TriggerEntity.class);
    }

    @DELETE
    @UnitOfWork
    public TriggerEntity delete(@Auth Credentials credentials, TriggerEntity triggerEntity) throws SchedulerException {
        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());
        unScheduleJobTrigger(entity);
        return quartzTriggerDao.delete(entity);
    }

    @POST
    @Path("/update")
    @UnitOfWork
    public TriggerEntity setCron(TriggerEntity triggerEntity) {
        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());
//        entity.setActive(triggerEntity.isActive());
//        entity.setCronExpression(triggerEntity.getCronExpression());
//        entity.setFireTime(triggerEntity.getFireTime());
        quartzTriggerDao.update(entity);
        return entity;
    }


    @POST
    @Path("/run")
    @UnitOfWork
    public TriggerEntity fireTrigger(TriggerEntity triggerEntity) throws SchedulerException {
        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());
        return scheduleJob(entity);
    }

    @POST
    @Path("/stop")
    @UnitOfWork
    public TriggerEntity stopTrigger(TriggerEntity triggerEntity) throws SchedulerException {
        TriggerEntity entity = quartzTriggerDao.findById(triggerEntity.getOid());
        return unScheduleJobTrigger(entity);

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
//        Scheduler scheduler = managedQuartz.getScheduler();
        JobInfo jobInfo = triggerEntity.getJob();
//        scheduler.pauseTrigger(TriggerKey.triggerKey(triggerEntity.getOid(), jobInfo.getOid()));
//        triggerEntity.setActive(false);
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
//        String cronExpression = triggerEntity.getCronExpression();
//        JobInfo jobInfo = triggerEntity.getJob();
//        Scheduler scheduler = managedQuartz.getScheduler();
//        JobDetail jobDetail = scheduler.getJobDetail(new JobKey(jobInfo.getOid(), QuartzBundle.STATIC_GROUP));
//        if (cronExpression != null) {
//            TriggerKey triggerKey = new TriggerKey(triggerEntity.getOid(), jobInfo.getOid());
//            TriggerBuilder<Trigger> trigger = newTrigger().startAt(new Date(triggerEntity.getFireTime())).withIdentity(triggerKey);
//            Set<Trigger> triggers = new LinkedHashSet<Trigger>();
//            triggers.add(trigger.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build());
//            scheduler.scheduleJob(jobDetail, triggers, true);
//            triggerEntity.setActive(true);
        return quartzTriggerDao.update(triggerEntity);
//        }
//        return triggerEntity;
    }


}
