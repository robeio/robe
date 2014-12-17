package io.robe.quartz;


import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Helps to manage all of the jobs from one place.
 */
public class JobManager {

    private static boolean initialized = false;
    private static JobManager instance;
    private static final Object lock = new Object();

    //TODO: Convert to factory later
    private final Scheduler scheduler;

    private JobManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    protected static synchronized void initialize(Scheduler scheduler) {
        if (initialized)
            throw new RuntimeException("Manager is already Initialized");
        else
            instance = new JobManager(scheduler);
    }

    public static JobManager getInstance() {
        return instance;
    }


    public void scheduleJob(JobDetail jobDetail, Set<Trigger> triggers, boolean replace) throws SchedulerException {
        synchronized (lock) {
            scheduler.scheduleJob(jobDetail, triggers, replace);
        }
    }

    public boolean unScheduleJob(String name, String group) throws SchedulerException {
        synchronized (lock) {
            return scheduler.unscheduleJob(TriggerKey.triggerKey(name, group));
        }
    }

    public boolean checkExists(String name, String group) throws SchedulerException {
        synchronized (lock) {
            return scheduler.checkExists(TriggerKey.triggerKey(name, group));
        }
    }

    public Trigger getTrigger(String name, String group) throws SchedulerException {
        synchronized (lock) {
            return scheduler.getTrigger(TriggerKey.triggerKey(name, group));
        }
    }

    private List<? extends Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobKey);
    }

    public List<? extends Trigger> getTriggersOfJob(String name, String group) throws SchedulerException {
        return getTriggersOfJob(JobKey.jobKey(name, group));
    }

    public List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException {
            return scheduler.getCurrentlyExecutingJobs();
    }

    public List<JobKey> getAllScheduledJobs() throws SchedulerException {
        List<JobKey> jobKeys = new LinkedList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            jobKeys.addAll(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)));
        }
        return jobKeys;
    }

    public List<JobKey> getGeneralReport() throws SchedulerException {

        //TODO: sout is only for dev. Will change after deciding report
        List<JobKey> jobKeys = new LinkedList<>();
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                for (Trigger trigger : getTriggersOfJob(jobKey)) {
                    StringBuilder builder = new StringBuilder();
                    builder.append(jobKey.getGroup()).append(" ,");
                    builder.append(jobKey.getName()).append(" ,");
                    builder.append(trigger.getKey()).append(" ,");
                    builder.append(trigger.getDescription()).append(" ,");
                    builder.append(trigger.getStartTime()).append(" ,");
                    builder.append(trigger.getEndTime()).append(" ,");
                    builder.append(trigger.getPreviousFireTime()).append(" ,");
                    builder.append(trigger.getNextFireTime()).append(" ,");
                    builder.append(getTriggerStatus(trigger.getKey()).name());
                    System.out.println(builder.toString());
                }
            }
        }
        return jobKeys;
    }

    public Trigger.TriggerState getTriggerStatus(TriggerKey key) throws SchedulerException {
        return scheduler.getTriggerState(key);
    }

    public void pauseJob(JobKey key) throws SchedulerException {
        scheduler.pauseJob(key);
    }
    public void pauseTrigger(TriggerKey key) throws SchedulerException {
        scheduler.pauseTrigger(key);
    }

}
