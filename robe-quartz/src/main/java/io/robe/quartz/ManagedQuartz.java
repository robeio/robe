package io.robe.quartz;

import com.google.common.collect.Sets;
import io.dropwizard.lifecycle.Managed;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;


public class ManagedQuartz implements Managed {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedQuartz.class);
    private Set<JobDetail> onStartJobs;
    private Set<JobDetail> onStopJobs;

    public ManagedQuartz() {
        this.onStartJobs = new HashSet<>();
        this.onStopJobs = new HashSet<>();
    }

    public ManagedQuartz(Set<JobDetail> onStartJobs, Set<JobDetail> onStopJobs) {
        this.onStartJobs = onStartJobs;
        this.onStopJobs = onStopJobs;
    }

    @Override
    public void start() throws SchedulerException {
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
        if (JobManager.getInstance() != null)
            JobManager.getInstance().shutdown(true);
    }

    private void scheduleAllJobsOnApplicationStop() throws SchedulerException {
        for (JobDetail detail : onStopJobs) {
            if (!JobManager.getInstance().checkExists(detail.getKey()))
                JobManager.getInstance().scheduleJob(detail, executeNowTrigger());
            else
                JobManager.getInstance().scheduleJob(detail, addNowTrigger(detail.getKey()), true);

        }
    }


    private void scheduleAllJobsOnApplicationStart() throws SchedulerException {
        LOGGER.info("Jobs to run on application start: " + onStartJobs);
        for (JobDetail detail : onStartJobs) {
            if (!JobManager.getInstance().checkExists(detail.getKey()))
                JobManager.getInstance().scheduleJob(detail, executeNowTrigger());
            else
                JobManager.getInstance().scheduleJob(detail, addNowTrigger(detail.getKey()), true);
        }
    }

    private Trigger executeNowTrigger() {
        return TriggerBuilder.newTrigger().startNow().build();
    }

    private Set<Trigger> addNowTrigger(JobKey key) throws SchedulerException {
        Set<Trigger> triggers = Sets.newHashSet(JobManager.getInstance().getTriggersOfJob(key.getName(), key.getGroup()));
        triggers.add(executeNowTrigger());
        return triggers;

    }


}
