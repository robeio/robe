package io.robe.quartz;

import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.QuartzJob;
import org.quartz.*;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Handles with user defined Cron from admin panel
 */
public class JobScheduler {

    @Inject
    ManagedQuartz managedQuartz;


    /**
     * Gets trigger and job values from QuartzJob and make them scheduleJob.
     *
     * @param quartzJob
     * @return
     */
    public String scheduleJob(QuartzJob quartzJob) {
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
