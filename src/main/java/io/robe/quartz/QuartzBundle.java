package io.robe.quartz;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import io.robe.hibernate.HibernateBundle;
import io.robe.hibernate.HibernateConfiguration;
import io.robe.hibernate.entity.QuartzJob;
import io.robe.service.RobeServiceConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzBundle implements ConfiguredBundle<RobeServiceConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzBundle.class);

    Scheduler scheduler = null;
    Set<Class<? extends Job>> onStartJobs = null;
    Set<Class<? extends Job>> onStopJobs = null;

    RobeServiceConfiguration configuration;
    HibernateBundle hibernateBundle;

    public QuartzBundle(HibernateBundle hibernateBundle) {
        this.hibernateBundle = hibernateBundle;
    }




    @Override
    public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
        this.configuration = configuration;
        initializeScheduler(hibernateBundle.getSessionFactory());

    }



    public void initializeScheduler(SessionFactory sessionFactory) throws Exception {
        final Session session = sessionFactory.openSession();

        Properties properties = new Properties();

        HibernateConfiguration hibernateConfiguration = configuration.getHibernateConfiguration();
        QuartzConfiguration quartzConfiguration = configuration.getQuartzConfiguration();


        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStoreClass());
        properties.setProperty("org.quartz.scheduler.instanceName", quartzConfiguration.getInstanceName());
        properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(quartzConfiguration.getThreadCount()));
        properties.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(quartzConfiguration.getThreadPriority()));
        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStoreClass());

        if (!"org.quartz.simpl.RAMJobStore".equals(quartzConfiguration.getJobStoreClass())) {
            properties.setProperty("org.quartz.jobStore.dataSource", "myDS");
            properties.setProperty("org.quartz.dataSource.myDS.driver", hibernateConfiguration.getDriverClass());
            properties.setProperty("org.quartz.dataSource.myDS.URL", hibernateConfiguration.getUrl());
            properties.setProperty("org.quartz.dataSource.myDS.user", hibernateConfiguration.getUser());
            properties.setProperty("org.quartz.dataSource.myDS.password", hibernateConfiguration.getPassword());
            properties.setProperty("org.quartz.dataSource.myDS.maxConnections", String.valueOf(quartzConfiguration.getMaxConnections()));
            properties.setProperty("org.quartz.jobStore.tablePrefix", quartzConfiguration.getTablePrefix());
            properties.setProperty("org.quartz.jobStore.driverDelegateClass", quartzConfiguration.getDriverDelegateClass());
        }
        SchedulerFactory factory = new StdSchedulerFactory(properties);

        try {
            scheduler = factory.getScheduler();
            scheduler.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        String[] jobPackage = quartzConfiguration.getJobPackage().split(",");
        Set<Class<? extends Job>> jobClasses;
        for (String myPackage : jobPackage) {
            Reflections reflections = new Reflections(myPackage);
            jobClasses = reflections.getSubTypesOf(Job.class);

            onStartJobs = new HashSet<Class<? extends Job>>();
            onStopJobs = new HashSet<Class<? extends Job>>();


            for (Class<? extends Job> clazz : jobClasses) {
                if (clazz.isAnnotationPresent(OnApplicationStart.class))
                    onStartJobs.add(clazz);
                if (clazz.isAnnotationPresent(OnApplicationStop.class))
                    onStopJobs.add(clazz);

            }
            scheduleJob(jobClasses, scheduler, session);
        }
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }


    /**
     * Schedule all classes with @quartz annotation
     *
     * @param jobClassses
     * @param scheduler
     * @param session
     * @return
     * @throws SchedulerException
     */
    private void scheduleJob(Set<Class<? extends Job>> jobClassses, Scheduler scheduler, Session session) throws SchedulerException {

        for (Class<? extends Job> jobClass : jobClassses) {
            Scheduled scheduledAnnotation = jobClass.getAnnotation(Scheduled.class);
            if (scheduledAnnotation != null) {
                String jobName = jobClass.getName();
                if (scheduledAnnotation.manager().equals(Scheduled.Manager.ANNOTATION)) {
                    JobDetail job = newJob(jobClass).
                            withIdentity(jobName).
                            build();
                    Trigger trigger = buildTrigger(scheduledAnnotation);
                    Set<Trigger> triggers = new LinkedHashSet<Trigger>();
                    triggers.add(trigger);
                    scheduler.scheduleJob(job, triggers, true);
                    LOGGER.info("Scheduled job : " + job.toString() + " with trigger : " + trigger.toString());
                } else if (scheduledAnnotation.manager().equals(Scheduled.Manager.DB)) {
                    List<QuartzJob> quartzJobList = session.createCriteria(QuartzJob.class).add(Restrictions.eq("jobClassName", jobClass.getName())).list();
                    QuartzJob quartzJob;
                    if (quartzJobList.size() < 1) {
                        quartzJob = new QuartzJob();
                        quartzJob.setJobClassName(jobClass.getName());
                        quartzJob.setCronExpression(scheduledAnnotation.cron());
                        quartzJob.setSchedulerName(scheduler.getSchedulerName());
                        session.persist(quartzJob);
                    }
                    else if(quartzJobList.size() ==1){
                        quartzJob = quartzJobList.get(0);
                        Lighter lighter = new Lighter();
                    }
                }
            }
        }
        session.flush();
        session.close();
    }

    /**
     * Builds cron trigger with parameters of @quartz annotation
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

    public Scheduler getScheduler() {
        return scheduler;
    }

    public Set<Class<? extends Job>> getOnStartJobs() {
        return onStartJobs;
    }

    public Set<Class<? extends Job>> getOnStopJobs() {
        return onStopJobs;
    }


}
