package io.robe.quartz;

import com.yammer.dropwizard.lifecycle.Managed;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;


public class ManagedQuartz implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedQuartz.class);
    private Set<Class<? extends Job>> onStartJobs;
    private Set<Class<? extends Job>> onStopJobs;
    private Scheduler scheduler;

    public ManagedQuartz(Scheduler scheduler,Set<Class<? extends Job>> onStartJobs, Set<Class<? extends Job>> onStopJobs) {
        checkNotNull(scheduler);
        this.scheduler = scheduler;
        this.onStartJobs = onStartJobs;
        this.onStopJobs = onStopJobs;
    }

    @Override
    public void start() throws SchedulerException {
        scheduler.start();
        scheduleAllJobsOnApplicationStart();
    }

    @Override
    public void stop() throws SchedulerException {
        scheduleAllJobsOnApplicationStop();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOGGER.info("Finished onStop Jobs Shutting down the application.");
        }
        scheduler.shutdown(true);
    }

    private void scheduleAllJobsOnApplicationStop() throws SchedulerException {
        for (Class<? extends Job> clazz : onStopJobs) {
            JobBuilder jobDetail = JobBuilder.newJob(clazz);
            scheduler.scheduleJob(jobDetail.build(), executeNowTrigger());
        }
    }


    private void scheduleAllJobsOnApplicationStart() throws SchedulerException {
        LOGGER.info("Jobs to run on application start: " + onStartJobs);
        for (Class<? extends org.quartz.Job> clazz : onStartJobs) {
            JobBuilder jobBuilder = JobBuilder.newJob(clazz);
            scheduler.scheduleJob(jobBuilder.build(), executeNowTrigger());
        }
    }

    private Trigger executeNowTrigger() {
        return TriggerBuilder.newTrigger().startNow().build();
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
