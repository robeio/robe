package io.robe.admin.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.entity.QuartzJob;
import io.robe.auth.Credentials;
import io.robe.quartz.ManagedQuartz;
import org.apache.log4j.Logger;
import org.quartz.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.quartz.TriggerBuilder.newTrigger;

@Path("quartzJob")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuartzJobResource {
    @Inject
    QuartzJobDao quartzJobDao;
    @Inject
    ManagedQuartz managedQuartz;

    private static Logger LOGGER = Logger.getLogger(QuartzJobResource.class);

    @GET
    @UnitOfWork
    public List<QuartzJob> getAll(@Auth Credentials credentials) {
        List<QuartzJob> list = quartzJobDao.findAll(QuartzJob.class);
        return list;
    }

    @POST
    @Path("/update")
    @UnitOfWork
    public QuartzJob setCron(QuartzJob quartzJob) {
        quartzJob.setActive(false);
        quartzJobDao.update(quartzJob);
        return quartzJob;
    }

    @POST
    @Path("/fire")
    @UnitOfWork
    public QuartzJob fireJob(QuartzJob quartzJob) throws SchedulerException {
        QuartzJob cron = scheduleJob(quartzJob);
        return quartzJob;
    }

    /**
     * Gets QuartzJob instance and fire related job with its trigger.
     *
     * @param quartzJob choosen Job instance
     * @return quartzjob instance
     * @throws SchedulerException
     * @throws
     */
    private QuartzJob scheduleJob(QuartzJob quartzJob) throws SchedulerException {
        String cronExpression = quartzJob.getCronExpression();
        Scheduler scheduler = managedQuartz.getScheduler();
        JobDetail jobDetail = scheduler.getJobDetail(new JobKey(quartzJob.getJobClassName(), "DynamicCronJob"));
        if (cronExpression != null) {
            TriggerKey triggerKey = new TriggerKey(quartzJob.getJobClassName(), "DynamicCronTrigger");
            boolean unscheduleResult = scheduler.unscheduleJob(triggerKey);
            LOGGER.info(quartzJob.getJobClassName() +"unscheduling process returns with : " +unscheduleResult);
            TriggerBuilder<Trigger> trigger = newTrigger().withIdentity(triggerKey);
            trigger.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
            // Set of triggers will be user for multiple triggers later.
            Set<Trigger> triggers = new LinkedHashSet<Trigger>();
            triggers.add(trigger.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build());
            scheduler.scheduleJob(jobDetail, triggers, true);
            quartzJob.setActive(true);
            quartzJobDao.update(quartzJob);
        }

        return quartzJob;
    }

}
