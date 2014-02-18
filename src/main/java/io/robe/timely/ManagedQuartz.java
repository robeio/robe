package io.robe.timely;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.yammer.dropwizard.lifecycle.Managed;
import org.hibernate.criterion.Restrictions;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static com.google.common.base.Preconditions.checkNotNull;


public class ManagedQuartz implements Managed {
    private static final Logger log = LoggerFactory.getLogger(ManagedQuartz.class);
    private Set<Class<? extends Job>> onStartJobs;
    private Set<Class<? extends Job>> onStopJobs;
    private Scheduler scheduler;

    @Inject
    public ManagedQuartz(Scheduler scheduler,Set<Class<? extends Job>> onStartJobs, Set<Class<? extends Job>> onStopJobs) {
        checkNotNull(scheduler);
        this.scheduler = scheduler;
        this.onStartJobs = onStartJobs;
        this.onStopJobs = onStopJobs;
    }



    @Override
    public void start() throws Exception {
        scheduler.start();
        scheduleAllJobsOnApplicationStart();
    }

    @Override
    public void stop() throws Exception {
        scheduleAllJobsOnApplicationStop();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {

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
        log.info("Jobs to run on application start: " + onStartJobs);
        for (Class<? extends org.quartz.Job> clazz : onStartJobs) {
            JobBuilder jobBuilder = JobBuilder.newJob(clazz);
            scheduler.scheduleJob(jobBuilder.build(), executeNowTrigger());
        }
    }

    private Trigger executeNowTrigger() {
        return TriggerBuilder.newTrigger().startNow().build();
    }
}
