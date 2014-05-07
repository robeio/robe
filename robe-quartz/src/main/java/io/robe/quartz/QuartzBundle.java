package io.robe.quartz;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import io.robe.hibernate.HasHibernateConfiguration;
import io.robe.quartz.annotation.ByAnnotation;
import io.robe.quartz.annotations.OnApplicationStart;
import io.robe.quartz.annotations.OnApplicationStop;
import io.robe.quartz.annotations.Scheduled;
import io.robe.quartz.annotations.ScheduledBy;
import io.robe.quartz.configuration.HasQuartzConfiguration;
import io.robe.quartz.configuration.QuartzConfiguration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class QuartzBundle<T extends Configuration & HasQuartzConfiguration & HasHibernateConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzBundle.class);
    public static final String DYNAMIC_GROUP = "DynamicCronJob";
    public static final String STATIC_GROUP = "StaticCronJob";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    Scheduler scheduler = null;
    Set<Class<? extends Job>> onStartJobs = null;
    Set<Class<? extends Job>> onStopJobs = null;

    private T configuration;


    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the service's {@link com.yammer.dropwizard.config.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(T configuration, Environment environment) throws Exception {
        this.configuration = configuration;
        initializeScheduler();
    }

    public void initializeScheduler() throws SchedulerException {

        Properties properties = new Properties();

        QuartzConfiguration quartzConfiguration = configuration.getQuartzConfiguration();

        //Set quartz properties.
        properties.setProperty("org.quartz.scheduler.instanceName", quartzConfiguration.getInstanceName());
        properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(quartzConfiguration.getThreadCount()));
        properties.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(quartzConfiguration.getThreadPriority()));
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", quartzConfiguration.getSkipUpdateCheck());

        //Set jobstore properties.
        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStore().getClassName());
        //Forward jobstore properties directly
        if (quartzConfiguration.getJobStore().getProperties() != null) {
            properties.putAll(quartzConfiguration.getJobStore().getProperties());
        }

        SchedulerFactory factory = new StdSchedulerFactory(properties);

        scheduler = factory.getScheduler();
        scheduler.start();

        String[] scanPackages = quartzConfiguration.getScanPackages();
        Set<Class<? extends Job>> quartzJobs;
        int jobsFound = 0;
        onStartJobs = new HashSet<Class<? extends Job>>();
        onStopJobs = new HashSet<Class<? extends Job>>();

        for (String scanPackage : scanPackages) {
            LOGGER.info("Scanning Jobs package : " + scanPackage);
            Reflections reflections = new Reflections(scanPackage);
            quartzJobs = reflections.getSubTypesOf(Job.class);
            for (Class<? extends Job> clazz : quartzJobs) {
                if (clazz.isAnnotationPresent(OnApplicationStart.class))
                    onStartJobs.add(clazz);
                if (clazz.isAnnotationPresent(OnApplicationStop.class))
                    onStopJobs.add(clazz);
            }
            jobsFound += quartzJobs.size();
            startSchedulingJobs(quartzJobs, scheduler);
        }
        LOGGER.info("Scanning Jobs Complete Total : " + jobsFound);

    }

    /**
     * Schedule all classes with @quartz annotation
     * If @Sheduled.Manager is ANNOTATION, it will schedule jobs with specified cron at run time
     * If @Sheduled.Manager is PROVIDER, it will schedule durable job; after ROBE start, user could create CRON expression on admin panel
     *
     * @param jobClassses Classes which implements job interface
     * @param scheduler   Quartz Job Scheduler
     * @return
     * @throws SchedulerException
     */
    private void startSchedulingJobs(Set<Class<? extends Job>> jobClassses, Scheduler scheduler) throws SchedulerException {
        ByAnnotation byAnnotation = new ByAnnotation();
        List<QuartzJob> jobs = new LinkedList<QuartzJob>();

        for (Class<? extends Job> clazz : jobClassses) {
            QuartzJob job = null;
            if (clazz.getAnnotation(Scheduled.class) != null) {
                job = byAnnotation.getQuartzJob(clazz);
                schedule(job);
            }
            ScheduledBy scheduledByAnnotation = clazz.getAnnotation(ScheduledBy.class);
            if (scheduledByAnnotation != null) {
                try {
                    job = scheduledByAnnotation.provider().newInstance().getQuartzJob(clazz);
                    if (job == null) {
                        LOGGER.warn("Job created for the first time " + clazz.getName() + " no cron definition ");
                    } else {
                        schedule(job);
                    }
                } catch (InstantiationException e) {
                    LOGGER.error("Cron Job Provider is not proper." + clazz.getName() + " : provider: " + scheduledByAnnotation.provider(), e);
                } catch (IllegalAccessException e) {
                    LOGGER.error("Cron Job Provider is not proper." + clazz.getName() + " : provider: " + scheduledByAnnotation.provider(), e);
                }
            }
        }

    }

    private void schedule(QuartzJob job) throws SchedulerException {
        List<QuartzTrigger> quartzTriggers = job.getTriggers();
        Set<Trigger> triggers = new LinkedHashSet<Trigger>();
        for (QuartzTrigger quartzTrigger : quartzTriggers) {
            TriggerBuilder<Trigger> trigger = newTrigger();
            String cron = quartzTrigger.getCronExpression();
            if (cron != null && cron.trim().length() > 0) {
                trigger.startAt(new Date(quartzTrigger.getFireTime())).withSchedule(CronScheduleBuilder.cronSchedule(cron));
                LOGGER.info(job.getClazz().getSimpleName() + " Trigger set to start at" + new Date(quartzTrigger.getFireTime()) + " with this cron definition : " + cron);
            } else
                LOGGER.warn(job.getClazz().getSimpleName() + " Trigger error at " + job.getOid() + " with this cron definition : " + cron);

            triggers.add(trigger.build());
        }
        if (!triggers.isEmpty()) {
            JobKey jobKey = JobKey.jobKey(job.getOid(), STATIC_GROUP);
            JobDetail jobDetail = newJob(job.getClazz()).
                    withIdentity(jobKey).
                    build();
            scheduler.scheduleJob(jobDetail, triggers, true);
            for (QuartzTrigger triggerEntity : quartzTriggers) {
                LOGGER.info(job.getClazz().getSimpleName() + " Scheduled with trigger : " + triggerEntity.getCronExpression() + " started at : " + new Date());
            }
            try {
                scheduler.addJob(jobDetail, true);
            } catch (SchedulerException e) {
                LOGGER.error("Can't schedule " + job.getClazz(), e);
            }

        }


    }


    /**
     * Initializes the service bootstrap.
     *
     * @param bootstrap the service bootstrap
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

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
