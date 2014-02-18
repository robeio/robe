package io.robe.timely;

import com.google.common.collect.Sets;
import com.yammer.dropwizard.lifecycle.Managed;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManagedQuartz implements Managed {
    private static final Logger log = LoggerFactory.getLogger(ManagedQuartz.class);
    private Reflections reflections = null;
    protected Scheduler scheduler;

    public ManagedQuartz() {

    }

    public ManagedQuartz(String scanUrl) {
        reflections = new Reflections(scanUrl);
    }

    @Override
    public void start() throws Exception {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        scheduleAllJobsOnApplicationStart();
    }

    @Override
    public void stop() throws Exception {
        scheduleAllJobsOnApplicationStop();

        try {
            Thread.sleep(100);
        }catch ( InterruptedException e){

        }

        scheduler.shutdown(true);
    }

    private void scheduleAllJobsOnApplicationStop() throws SchedulerException {
        List<Class<? extends Job>> stopJobClasses = getJobClasses(OnApplicationStop.class);
        for (Class<? extends Job> clazz : stopJobClasses) {
            JobBuilder jobDetail = JobBuilder.newJob(clazz);
            scheduler.scheduleJob(jobDetail.build(), executeNowTrigger());
        }
    }

    private List<Class<? extends Job>> getJobClasses(Class annotation) {
        Set<Class<? extends Job>> jobs = (Set<Class<? extends Job>>) reflections.getSubTypesOf(Job.class);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotation);

        return Sets.intersection(new HashSet<Class<? extends Job>>(jobs), annotatedClasses).immutableCopy().asList();
    }


    private void scheduleAllJobsOnApplicationStart() throws SchedulerException {
        List<Class<? extends Job>> startJobClasses = getJobClasses(OnApplicationStart.class);
        log.info("Jobs to run on application start: " + startJobClasses);
        for (Class<? extends org.quartz.Job> clazz : startJobClasses) {
            JobBuilder jobBuilder = JobBuilder.newJob(clazz);
            scheduler.scheduleJob(jobBuilder.build(), executeNowTrigger());
        }
    }

    private Trigger executeNowTrigger() {
        return  TriggerBuilder.newTrigger().startNow().build();
    }
}
