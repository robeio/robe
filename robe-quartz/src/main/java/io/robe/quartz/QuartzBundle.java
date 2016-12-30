package io.robe.quartz;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.robe.quartz.configuration.HasQuartzConfiguration;
import io.robe.quartz.configuration.QuartzConfiguration;
import io.robe.quartz.info.JobInfo;
import io.robe.quartz.info.JobInfoProvider;
import io.robe.quartz.info.TriggerInfo;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configures  Quartz.
 * Collect {@link JobInfoProvider} classes
 * Initializes scheduler
 * Collects all subtypes of {@link org.quartz.Job} annotated with {@link RobeJob} including {@link @RobeTrigger}'s
 * * Collects additional triggers from providers
 * * Registers them all for future control.
 * Holds application start and end triggered jobs for managed access.
 * Provides Job & Trigger Registery
 */
public class QuartzBundle<T extends Configuration & HasQuartzConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzBundle.class);
    private Set<JobDetail> onStartJobs = null;
    private Set<JobDetail> onStopJobs = null;
    public static final ConcurrentHashMap<String, JobInfo> JOBS = new ConcurrentHashMap<>();


    public QuartzBundle() {
    }


    /**
     * Initializes the environment. Forwards the configuration to Quartz.
     * Collect {@link JobInfoProvider} classes
     * Initializes scheduler
     * Collects all subtypes of {@link org.quartz.Job} annotated with {@link RobeJob} including {@link @RobeTrigger}'s
     * * Collects additional triggers from providers
     * * Registers them all for future control.
     *
     * @param configuration the configuration object
     * @param environment   the service's {@link io.dropwizard.setup.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(T configuration, Environment environment) {
        QuartzConfiguration qConf = configuration.getQuartz();
        try {

            initializeScheduler(qConf.getProperties());
            collectAndScheduleJobs(qConf.getScanPackages());

            environment.lifecycle().manage(new ManagedQuartz(getOnStartJobs(), getOnStopJobs()));

        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException:", e);
        }
    }

    /**
     * Initialize scheduler and start JobManager
     *
     * @param properties
     * @throws SchedulerException
     */
    private void initializeScheduler(Properties properties) throws SchedulerException {
        SchedulerFactory factory = new StdSchedulerFactory(properties);
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        JobManager.initialize(scheduler);
    }

    private void collectAndScheduleJobs(String[] packages) throws SchedulerException {
        Set<Class<? extends Job>> quartzJobs;

        onStartJobs = new HashSet<>();
        onStopJobs = new HashSet<>();

        for (String pkg : packages) {
            LOGGER.info("Scanning Jobs package : " + pkg);
            Reflections reflections = new Reflections(pkg);
            quartzJobs = reflections.getSubTypesOf(Job.class);

            for (Class<? extends Job> clazz : quartzJobs) {
                RobeJob infoAnn = clazz.getDeclaredAnnotation(RobeJob.class);
                if (infoAnn == null)
                    continue;

                JobInfoProvider infoProvider;
                try {
                    infoProvider = infoAnn.provider().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                Set<Trigger> triggers = new HashSet<>();

                JobInfo info = infoProvider.getJob(clazz);
                if (info == null || JOBS.containsKey(info.getJobClass().getName()))
                    continue;


                JobDetail detail = JobInfoProvider.convert2JobDetail(info);

                StringBuilder logBuilder = new StringBuilder();
                //Collect all triggers
                for (TriggerInfo tInfo : info.getTriggers()) {
                    logBuilder.append("\n\t\tTrigger: ")
                            .append(clazz.getName())
                            .append("\tName: ")
                            .append(tInfo.getName())
                            .append("\tType:")
                            .append(tInfo.getType().name());
                    switch (tInfo.getType()) {
                        case ON_APP_START:
                            onStartJobs.add(detail);
                            break;
                        case ON_APP_STOP:
                            onStopJobs.add(detail);
                            break;
                        default:
                            triggers.add(JobInfoProvider.convert2Trigger(tInfo, info));
                    }
                }
                LOGGER.info("\n\tClass {} \n\tName: {} \n\tDesc: {} \n\t Triggers: {}",
                        info.getJobClass().getName(),
                        info.getName(),
                        info.getDescription(),
                        logBuilder.toString()
                );
                JobManager.getInstance().scheduleJob(detail, triggers, true);
                JOBS.put(info.getJobClass().getName(), info);
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
        LOGGER.info("Initializing QuartzBundle");
    }

    public Set<JobDetail> getOnStartJobs() {
        return onStartJobs;
    }

    public Set<JobDetail> getOnStopJobs() {
        return onStopJobs;
    }
}
