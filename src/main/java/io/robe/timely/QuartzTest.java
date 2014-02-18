package io.robe.timely;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by sinanselimoglu on 14/02/14.
 */
public class QuartzTest extends SimpleScheduleBuilder {

    public void schedulerTest() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            JobDetail jobDetail = JobBuilder.newJob(HelloQuartz.class)
                    .usingJobData("shout", "hurra")
                    .withIdentity("buBirKey")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .usingJobData("Says", "Hello Robe Io")
                    .startNow()
                    .withSchedule(simpleSchedule()
                            .withIntervalInSeconds(1)
                            .repeatForever())
                    .forJob(jobDetail)
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            scheduler.shutdown();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }


    }
}
