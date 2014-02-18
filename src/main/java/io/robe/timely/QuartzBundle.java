package io.robe.timely;

import ch.qos.logback.core.spi.LifeCycle;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import io.robe.hibernate.DBConfiguration;
import io.robe.service.RobeServiceConfiguration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by sinanselimoglu on 14/02/14.
 */
public class QuartzBundle implements ConfiguredBundle<RobeServiceConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzBundle.class);

    @Override
    public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {

        Properties properties = new Properties();

        DBConfiguration dbConfiguration = configuration.getDatabaseConfiguration();
        QuartzConfiguration quartzConfiguration = configuration.getQuartzConfiguration();

        properties.setProperty("org.quartz.jobStore.dataSource", "myDS");
        properties.setProperty("org.quartz.dataSource.myDS.driver", dbConfiguration.getDriverClass());
        properties.setProperty("org.quartz.dataSource.myDS.URL", dbConfiguration.getUrl());
        properties.setProperty("org.quartz.dataSource.myDS.user", dbConfiguration.getUser());
        properties.setProperty("org.quartz.dataSource.myDS.password", dbConfiguration.getPassword());

        properties.setProperty("org.quartz.scheduler.instanceName", quartzConfiguration.getInstanceName());
        properties.setProperty("org.quartz.dataSource.myDS.maxConnections", String.valueOf(quartzConfiguration.getMaxConnections()));
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", quartzConfiguration.getDriverDelegateClass());
        properties.setProperty("org.quartz.jobStore.tablePrefix", quartzConfiguration.getTablePrefix());
        properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(quartzConfiguration.getThreadCount()));
        properties.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(quartzConfiguration.getThreadPriority()));
        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStoreClass());
        properties.setProperty("org.quartz.jobStore.tablePrefix", quartzConfiguration.getTablePrefix());
        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStoreClass());

        SchedulerFactory factory = new StdSchedulerFactory(properties);

        Scheduler scheduler = null;
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
            scheduleJob(classes, scheduler);
        }



    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }


    /**
     * Schedule all classes with @timely annotation
     *
     * @param timelyClassses
     * @param scheduler
     * @return
     * @throws SchedulerException
     */
    private void scheduleJob(Set<Class<? extends Job>> timelyClassses, Scheduler scheduler) throws SchedulerException {
        for (Class<? extends Job> timelyClass : timelyClassses) {
            Scheduled scheduledAnnotation = timelyClass.getAnnotation(Scheduled.class);
            if (scheduledAnnotation != null) {
                JobDetail job = newJob(timelyClass).
                        build();
                Trigger trigger = buildTrigger(scheduledAnnotation);
                scheduler.scheduleJob(job, trigger);
                LOGGER.info("Scheduled job : " + job.toString() + " with trigger : " + trigger.toString());

            } else
                LOGGER.info("There is no annotated class with @Scheduled");
        }
    }

    /**
     * Builds cron trigger with parameters of @timely annotation
     *
     * @param scheduledAnnotation
     * @return trigger
     */
    private Trigger buildTrigger(Scheduled scheduledAnnotation) {
        TriggerBuilder<Trigger> trigger = newTrigger();
        if (scheduledAnnotation.cron() != null && scheduledAnnotation.cron().trim().length() > 0) {
            trigger.withSchedule(CronScheduleBuilder.cronSchedule(scheduledAnnotation.cron()));
        } else
            throw new IllegalArgumentException("You need cron definition for the @Scheduled annotation");

        return trigger.build();
    }


}
