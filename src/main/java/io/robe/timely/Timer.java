package io.robe.timely;

import io.robe.hibernate.DBConfiguration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.jdbcjobstore.JobStoreTX;
import org.quartz.simpl.RAMJobStore;
import org.quartz.spi.JobStore;
import org.quartz.spi.OperableTrigger;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 */
public class Timer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Timer.class);

    /**
     * Timer gets package names that includes job classes and schedule all jobs with @timely annotation
     * @param quartzConfiguration Quartz configuration
     */
    public Timer(QuartzConfiguration quartzConfiguration, DBConfiguration dbConfiguration) throws SchedulerException {
        Properties properties = new Properties();
        properties.setProperty("org.quartz.jobStore.dataSource","myDS");
        properties.setProperty("org.quartz.dataSource.myDS.driver",dbConfiguration.getDriverClass());
        properties.setProperty("org.quartz.dataSource.myDS.URL",dbConfiguration.getUrl());
        properties.setProperty("org.quartz.dataSource.myDS.user",dbConfiguration.getUser());
        properties.setProperty("org.quartz.dataSource.myDS.password",dbConfiguration.getPassword());

        properties.setProperty("org.quartz.scheduler.instanceName", quartzConfiguration.getInstanceName());
        properties.setProperty("org.quartz.dataSource.myDS.maxConnections", String.valueOf(quartzConfiguration.getMaxConnections()));
        properties.setProperty("org.quartz.jobStore.driverDelegateClass",quartzConfiguration.getDriverDelegateClass());
        properties.setProperty("org.quartz.jobStore.tablePrefix",quartzConfiguration.getTablePrefix());
        properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(quartzConfiguration.getThreadCount()));
        properties.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(quartzConfiguration.getThreadPriority()));
        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStoreClass());
        properties.setProperty("org.quartz.jobStore.tablePrefix",quartzConfiguration.getTablePrefix());
        properties.setProperty("org.quartz.jobStore.class",quartzConfiguration.getJobStoreClass());

        SchedulerFactory factory =new StdSchedulerFactory(properties);

        Scheduler scheduler=null;
        try {
            scheduler = factory.getScheduler();
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        String[] jobPackage = quartzConfiguration.getJobPackage().split(",");
        Set<Class<? extends Job>> classes;
        for (String myPackage : jobPackage) {
            Reflections reflections = new Reflections(myPackage);
            classes = reflections.getSubTypesOf(Job.class);
            scheduleJob(classes,scheduler);
        }


    }

    /**
     * Schedule all classes with @timely annotation
     * @param timelyClassses
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    private void scheduleJob(Set<Class<? extends Job>> timelyClassses, Scheduler scheduler) throws SchedulerException {
        for (Class<? extends Job> timelyClass : timelyClassses) {
            Timely timelyAnnotation = timelyClass.getAnnotation(Timely.class);
            if (timelyAnnotation != null) {
                JobDetail job = newJob(timelyClass).
                        build();
                Trigger trigger = buildTrigger(timelyAnnotation);
                scheduler.scheduleJob(job, trigger);
                LOGGER.info("Scheduled job : "+ job.toString() +" with trigger : " + trigger.toString());

            }
            else
                LOGGER.info("There is no annotated class with @Timely");
        }
    }

    /**
     * Builds cron trigger with parameters of @timely annotation
     * @param timelyAnnotation
     * @return trigger
     */
    private Trigger buildTrigger(Timely timelyAnnotation) {
        TriggerBuilder<Trigger> trigger = newTrigger();
        if (timelyAnnotation.cron() != null && timelyAnnotation.cron().trim().length() > 0) {
            trigger.withSchedule(CronScheduleBuilder.cronSchedule(timelyAnnotation.cron()));
        } else
            throw new IllegalArgumentException("You need cron definition for the @Timely annotation");

        return trigger.build();
    }
}
