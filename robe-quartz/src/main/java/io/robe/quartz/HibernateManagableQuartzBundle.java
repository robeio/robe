package io.robe.quartz;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import io.robe.hibernate.HasHibernateConfiguration;
import io.robe.hibernate.HibernateBundle;
import io.robe.hibernate.HibernateConfiguration;
import io.robe.quartz.annotations.OnApplicationStart;
import io.robe.quartz.annotations.OnApplicationStop;
import io.robe.quartz.annotations.Scheduled;
import io.robe.quartz.annotations.ScheduledBy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class HibernateManagableQuartzBundle<T extends Configuration & HasQuartzConfiguration & HasHibernateConfiguration> implements ConfiguredBundle<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateManagableQuartzBundle.class);
    public static final String DYNAMIC_GROUP = "DynamicCronJob";
    public static final String STATIC_GROUP = "StaticCronJob";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    Scheduler scheduler = null;
    Set<Class<? extends Job>> onStartJobs = null;
    Set<Class<? extends Job>> onStopJobs = null;

    T configuration;

    HibernateBundle hibernateBundle;

    public HibernateManagableQuartzBundle(HibernateBundle hibernateBundle) {
        this.hibernateBundle = hibernateBundle;
    }


    @Override
    public void run(T configuration, Environment environment) throws SchedulerException {
        this.configuration = configuration;
        initializeScheduler(hibernateBundle);
    }


    public void initializeScheduler(HibernateBundle hibernateBundle) throws SchedulerException {
        SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
        Session session = sessionFactory.openSession();

        Properties properties = new Properties();

        HibernateConfiguration hibernateConfiguration = configuration.getHibernateConfiguration();
        QuartzConfiguration quartzConfiguration = configuration.getQuartzConfiguration();


        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStoreClass());
        properties.setProperty("org.quartz.scheduler.instanceName", quartzConfiguration.getInstanceName());
        properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(quartzConfiguration.getThreadCount()));
        properties.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(quartzConfiguration.getThreadPriority()));
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", quartzConfiguration.getSkipUpdateCheck());

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

        scheduler = factory.getScheduler();
        scheduler.start();

        String[] jobPackage = quartzConfiguration.getScanPackages();
        Set<Class<? extends Job>> jobClasses;
        for (String myPackage : jobPackage) {
            LOGGER.info("Job class packages : " + myPackage);
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
     * If @Sheduled.Manager is ANNOTATION, it will schedule jobs with specified cron at run time
     * If @Sheduled.Manager is PROVIDER, it will schedule durable job; after ROBE start, user could create CRON expression on admin panel
     *
     * @param jobClassses Classes which implements job interface
     * @param scheduler   Quartz Job Scheduler
     * @param session     Hibernate session
     * @return
     * @throws SchedulerException
     */
    private void scheduleJob(Set<Class<? extends Job>> jobClassses, Scheduler scheduler, Session session) throws SchedulerException {

        for (Class<? extends Job> jobClass : jobClassses) {
            Scheduled scheduledAnnotation = jobClass.getAnnotation(Scheduled.class);
            if (scheduledAnnotation != null) {
                schedule(scheduler, jobClass, scheduledAnnotation);
            }
            ScheduledBy scheduledByAnnotation = jobClass.getAnnotation(ScheduledBy.class);
            if (scheduledByAnnotation != null) {
                scheduleBy(scheduler, session, jobClass, scheduledByAnnotation);
            }
        }

        session.flush();
        session.close();
    }

    private void scheduleBy(Scheduler scheduler, Session session, Class<? extends Job> jobClass, ScheduledBy annotation) throws SchedulerException {
        String jobName = jobClass.getName();
        QuartzJob quartzJob = getOldJob(jobName, session);

        if (quartzJob != null) {
            /*
                ! - Quartz Job Configuration Dependency - !
                If DevGuy selects Ram job store, jobs will be deactive when server restarts,
                If DevGuy selects DB job store, jobs which are already active stays in active state when server starts
            */
            if (configuration.getQuartzConfiguration().getJobStoreClass().equals("org.quartz.simpl.RAMJobStore"))
                deactivateAllTriggers(quartzJob, session);
            LOGGER.info(jobName + " named job recovered from PROVIDER ");
        } else {
            quartzJob = new QuartzJob();
            quartzJob.setJobClassName(jobName);
            quartzJob.setSchedulerName(scheduler.getSchedulerName());
            quartzJob.setDescription(annotation.description());
            session.persist(quartzJob);
            LOGGER.info(jobName + " Job saved to database");
        }
        //Check Trigger schedule if available
        if (annotation.autoStart()) {
            List<QuartzTrigger> triggerEntries = quartzJob.getTriggers();
            Set<Trigger> triggers = new LinkedHashSet<Trigger>();
            for (QuartzTrigger quartzTrigger : triggerEntries) {
                TriggerBuilder<Trigger> trigger = newTrigger();
                String cron = quartzTrigger.getCronExpression();
                if (cron != null && cron.trim().length() > 0) {
                    trigger.startAt(new Date(quartzTrigger.getFireTime())).withSchedule(CronScheduleBuilder.cronSchedule(cron));
                    LOGGER.info("Trigger set to start at" + new Date(quartzTrigger.getFireTime()) + " with this cron definition : " + cron);
                } else
                    throw new IllegalArgumentException("You need to define cron expression if you want to set cron expression from admin panel, choose manager PROVIDER");

                triggers.add(trigger.build());
            }
            if (!triggers.isEmpty()) {
                JobKey jobKey = JobKey.jobKey(jobName, STATIC_GROUP);
                JobDetail job = newJob(jobClass).
                        withIdentity(jobKey).
                        build();
                scheduler.scheduleJob(job, triggers, true);
                for (QuartzTrigger quartzTrigger : triggerEntries) {
                    LOGGER.info("Scheduled job : " + job.toString() + " with trigger : " + quartzTrigger.getCronExpression() + " started at : " + new Date());
                }
            }
        } else {
            JobKey jobKey = JobKey.jobKey(quartzJob.getOid(), DYNAMIC_GROUP);
            JobDetail jobDetail = newJob(jobClass).storeDurably().withIdentity(jobKey).build();
            scheduler.addJob(jobDetail, true);
        }
    }


    private void schedule(Scheduler scheduler, Class<? extends Job> jobClass, Scheduled scheduledAnnotation) throws SchedulerException {
        String jobName = jobClass.getName();
        JobKey jobKey = JobKey.jobKey(jobName, STATIC_GROUP);
        JobDetail job = newJob(jobClass).
                withIdentity(jobKey).
                build();
        //TODO: DevGuy may add multiple cron expression inside of annotation
        Trigger trigger = buildTrigger(scheduledAnnotation);
        Set<Trigger> triggers = new LinkedHashSet<Trigger>();
        triggers.add(trigger);
        scheduler.scheduleJob(job, triggers, true);
        LOGGER.info("Scheduled job : " + job.toString() + " with trigger : " + trigger.toString() + " started at : " + new Date());
    }

    /**
     * Make all triggers 'active' status to false
     *
     * @param quartzJob QuartzJob which has triggers
     * @param session   Hibernate session
     */
    private void deactivateAllTriggers(QuartzJob quartzJob, Session session) {
        List<QuartzTrigger> quartzTriggerList = session.createCriteria(QuartzTrigger.class)
                .add(Restrictions.eq("jobId", quartzJob.getOid()))
                .add(Restrictions.eq("active", true)).list();
        if (!quartzTriggerList.isEmpty()) {
            for (QuartzTrigger quartTrigger : quartzTriggerList) {
                quartTrigger.setActive(false);
                session.persist(quartTrigger);
            }
        }
        LOGGER.info("All triggers belongs to : " + quartzJob.getJobClassName() + " deactivated..!");
    }

    /**
     * Builds cron trigger with parameters of @quartz annotation
     *
     * @param scheduledAnnotation Annotation which has cron expression
     * @return trigger
     */
    private Trigger buildTrigger(Scheduled scheduledAnnotation) {
        TriggerBuilder<Trigger> trigger = newTrigger();
        if (scheduledAnnotation.cron() != null && scheduledAnnotation.cron().trim().length() > 0) {
            try {
                trigger.startAt(FORMAT.parse(scheduledAnnotation.startTime())).withSchedule(CronScheduleBuilder.cronSchedule(scheduledAnnotation.cron()));
            } catch (ParseException e) {
                LOGGER.error("Trigger error to start at" + scheduledAnnotation.startTime() + " with this cron definition : " + scheduledAnnotation.cron(), e);

            }
            LOGGER.info("Trigger set to start at" + scheduledAnnotation.startTime() + " with this cron definition : " + scheduledAnnotation.cron());
        } else
            throw new IllegalArgumentException("You need to define cron expression if you want to set cron expression from admin panel, choose manager PROVIDER");

        return trigger.build();
    }

    /**
     * If that job parameter exist in database gets old job
     *
     * @param jobClassName Quartz Job class name
     * @param session      Hibernate session
     * @return
     */
    private QuartzJob getOldJob(String jobClassName, Session session) {
        List list = session.createCriteria(QuartzJob.class).add(Restrictions.eq("jobClassName", jobClassName)).list();
        if (list.size() < 1)
            return null;
        return (QuartzJob) list.get(0);
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
