package io.robe.admin.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.auth.Credentials;
import io.robe.quartz.ManagedQuartz;
import io.robe.quartz.QuartzJob;
import io.robe.quartz.QuartzTrigger;
import io.robe.quartz.annotations.Scheduled;
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
    public List<QuartzTrigger> getAll(@Auth Credentials credentials) {
        List<QuartzTrigger> triggerList = quartzTriggerDao.findAll(QuartzTrigger.class);
        return triggerList;
    }

    @DELETE
    @UnitOfWork
    public QuartzTrigger delete(@Auth Credentials credentials,QuartzTrigger quartzTrigger) throws SchedulerException {
        unScheduleJobTrigger(quartzTrigger);
        quartzTriggerDao.delete(quartzTrigger);
        return quartzTrigger;
    }

    @POST
    @Path("/update")
    @UnitOfWork
    public QuartzTrigger setCron(QuartzTrigger quartzTrigger) {
        quartzTrigger.setActive(false);
        quartzTriggerDao.update(quartzTrigger);
        return quartzTrigger;
    }


    @POST
    @Path("/run")
    @UnitOfWork
    public QuartzTrigger fireTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        QuartzTrigger firedQuartzJob = scheduleJob(quartzTrigger);
        return firedQuartzJob;
    }

    @POST
    @Path("/stop")
    @UnitOfWork
    public QuartzTrigger stopTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        QuartzTrigger stoppedQuarztTrigger = unScheduleJobTrigger(quartzTrigger);
        return stoppedQuarztTrigger;

    }

    @POST
    @Path("/add")
    @UnitOfWork
    public QuartzTrigger addTrigger(QuartzJob quartzJob) throws SchedulerException {
        QuartzTrigger newQuartzTrigger = createQuartzTrigger(quartzJob);
        return newQuartzTrigger;

    }

    /**
     * Creates a new QuartzTrigger by using parent quartzJob Id
     * @param quartzJob
     * @return
     */
    private QuartzTrigger createQuartzTrigger(QuartzJob quartzJob) {
        QuartzTrigger newQuartzTrigger = new QuartzTrigger();
        newQuartzTrigger.setJobId(quartzJob.getOid());
        quartzTriggerDao.create(newQuartzTrigger);
        return newQuartzTrigger;
    }

    /**
     * Unschedule specified trigger
     *
     * @param quartzTrigger
     * @return
     * @throws SchedulerException
     */
    public QuartzTrigger unScheduleJobTrigger(QuartzTrigger quartzTrigger) throws SchedulerException {
        Scheduler scheduler = managedQuartz.getScheduler();
        QuartzJob quartzJob = quartzJobDao.findById(quartzTrigger.getJobId());
        scheduler.pauseTrigger(TriggerKey.triggerKey(quartzTrigger.getOid(), quartzJob.getOid()));
        quartzTrigger.setActive(false);
        quartzTriggerDao.update(quartzTrigger);

        return quartzTrigger;
    }

    /**
     * Gets QuartzTrigger and fire with its job class
     *
     * @param quartzTrigger
     * @return fired quartzTrigger
     * @throws SchedulerException
     */
    public QuartzTrigger scheduleJob(QuartzTrigger quartzTrigger) throws SchedulerException {
        String cronExpression = quartzTrigger.getCronExpression();
        QuartzJob quartzJob = quartzJobDao.findById(quartzTrigger.getJobId());
        Scheduler scheduler = managedQuartz.getScheduler();
        JobDetail jobDetail = scheduler.getJobDetail(new JobKey(quartzJob.getOid(), Scheduled.DYNAMIC_GROUP));
        if (cronExpression != null) {
            TriggerKey triggerKey = new TriggerKey(quartzTrigger.getOid(), quartzJob.getOid());
            TriggerBuilder<Trigger> trigger = newTrigger().startAt(new Date(quartzTrigger.getFireTime())).withIdentity(triggerKey);
            Set<Trigger> triggers = new LinkedHashSet<Trigger>();
            triggers.add(trigger.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build());
            scheduler.scheduleJob(jobDetail, triggers, true);
            quartzTrigger.setActive(true);
            quartzTriggerDao.update(quartzTrigger);
        }
        return quartzTrigger;
    }


}
