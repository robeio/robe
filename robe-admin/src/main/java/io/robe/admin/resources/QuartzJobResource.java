package io.robe.admin.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.auth.Credentials;
import io.robe.quartz.ManagedQuartz;
import io.robe.quartz.QuartzJob;
import org.quartz.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.quartz.TriggerBuilder.newTrigger;

@Path("quartzJob")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuartzJobResource {
    @Inject
    QuartzJobDao quartzJobDao;
    @Inject
    ManagedQuartz managedQuartz;

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
        quartzJobDao.update(quartzJob);
        return quartzJob;
    }

    @POST
    @Path("/fire")
    @UnitOfWork
    public String fireJob(QuartzJob quartzJob) {
        String cron = scheduleJob(quartzJob);
        return cron;
    }


    private String scheduleJob(QuartzJob quartzJob) {
        String cronExpression = null;
        try {
            Scheduler scheduler = managedQuartz.getScheduler();
            JobDetail jobDetail = scheduler.getJobDetail(new JobKey(quartzJob.getJobClassName()));
            cronExpression = quartzJob.getCronExpression();
            if (cronExpression != null) {
                TriggerBuilder<Trigger> trigger = newTrigger();
                trigger.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));

                scheduler.scheduleJob(jobDetail, trigger.build());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return cronExpression;
    }

}
