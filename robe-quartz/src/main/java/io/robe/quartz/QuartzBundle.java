package io.robe.quartz;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.robe.quartz.common.JobInfo;
import io.robe.quartz.common.JobProvider;
import io.robe.quartz.common.TriggerInfo;
import io.robe.quartz.configuration.HasQuartzConfiguration;
import io.robe.quartz.configuration.QuartzConfiguration;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Configures  Quartz.
 * Collect {@link io.robe.quartz.common.JobProvider} classes
 * Initializes scheduler
 * Collects all subtypes of {@link org.quartz.Job} annotated with {@link io.robe.quartz.job.schedule.QJob} including {@link @QTrigger}'s
 * * Collects additional triggers from providers
 * * Registers them all for future control.
 * Holds application start and end triggered jobs for managed access.
 * Provides Job & Trigger Registery
 */
public class QuartzBundle<T extends Configuration & HasQuartzConfiguration > implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzBundle.class);
    private List<JobProvider> providers = null;
    private Set<JobDetail> onStartJobs = null;
    private Set<JobDetail> onStopJobs = null;


    public QuartzBundle() {
    }


    /**
     * Initializes the environment. Forwards the configuration to Quartz.
     * Collect {@link io.robe.quartz.common.JobProvider} classes
     * Initializes scheduler
     * Collects all subtypes of {@link org.quartz.Job} annotated with {@link io.robe.quartz.job.schedule.QJob} including {@link @QTrigger}'s
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

            initializeScheduler(extractProperties(qConf));
            collectProviders(qConf.getProviders());
            collectAndScheduleJobs(qConf.getScanPackages());

            environment.lifecycle().manage(new ManagedQuartz(getOnStartJobs(),getOnStopJobs()));

        } catch (SchedulerException e) {
            LOGGER.error("SchedulerException:", e);
        }
    }

    /**
     * Just sets necessary properties for quartz.
     *
     * @param quartzConfiguration
     * @return
     */
    private Properties extractProperties(QuartzConfiguration quartzConfiguration) {
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", quartzConfiguration.getInstanceName());
        properties.setProperty("org.quartz.threadPool.threadCount", String.valueOf(quartzConfiguration.getThreadCount()));
        properties.setProperty("org.quartz.threadPool.threadPriority", String.valueOf(quartzConfiguration.getThreadPriority()));
        properties.setProperty("org.quartz.threadPool.class", String.valueOf(quartzConfiguration.getThreadPoolClass()));
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", quartzConfiguration.getSkipUpdateCheck());
        //Set jobstore properties.
        properties.setProperty("org.quartz.jobStore.class", quartzConfiguration.getJobStore().getClassName());
        //Forward jobstore properties directly
        if (quartzConfiguration.getJobStore().getProperties() != null) {
            properties.putAll(quartzConfiguration.getJobStore().getProperties());
        }
        return properties;
    }

    /**
     * Initialize scheduler and start JobManager
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
                //Collect all provided triggers
                Set<Trigger> triggers = new HashSet<>();

                for (JobProvider provider : providers) {
                    JobInfo jInfo = provider.getJob(clazz);
                    if (jInfo == null)
                        continue;

                    LOGGER.info("\nJob " + jInfo.getClass().getName() + "\n\tName: " + jInfo.getName() + "\n\tDesc: " + jInfo.getDescription());

                    JobDetail jobDetail = JobProvider.convert2JobDetail(jInfo);

                    //Collect all triggers
                    for (TriggerInfo tInfo : jInfo.getTriggers()) {

                        LOGGER.info("Trigger " + clazz.getName() + "\n\tName: " + tInfo.getName() + "\n\tType: " + tInfo.getType().name());

                        if (tInfo.getType().equals(TriggerInfo.Type.ON_APP_START)) {
                            onStartJobs.add(jobDetail);
                        } else if (tInfo.getType().equals(TriggerInfo.Type.ON_APP_STOP)) {
                            onStopJobs.add(jobDetail);
                        } else {
                            triggers.add(JobProvider.convert2Trigger(tInfo));
                        }
                    }

                    JobManager.getInstance().scheduleJob(jobDetail, triggers, true);

                }

                //TODO: Register Triggers for future access.


            }
        }
    }

    /**
     * Collects all custom providers at scan packages.
     * Creates instances and stores for usage.
     *
     * @param packages
     */
    private void collectProviders(String[] packages) {
        providers = new LinkedList<>();
        for (String pkg : packages) {
            LOGGER.info("Scanning Provider package : " + pkg);
            Reflections reflections = new Reflections(pkg);
            Set<Class<? extends JobProvider>> classses = reflections.getSubTypesOf(JobProvider.class);
            for (Class<? extends JobProvider> clazz : classses) {
                try {
                    JobProvider provider = clazz.newInstance();
                    LOGGER.info("Provider found : " + clazz.getName());
                    providers.add(provider);
                } catch (Exception e) {
                    LOGGER.error(clazz.getName(), e);
                }
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
