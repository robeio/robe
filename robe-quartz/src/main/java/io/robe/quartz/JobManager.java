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

    private static final Object lock = new Object();
    private static JobManager instance;
    //TODO: Convert to factory later
    private final Scheduler scheduler;

    private JobManager(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    protected static synchronized void initialize(Scheduler scheduler) {
        if (instance != null)
            throw new RuntimeException("Manager is already Initialized");
        else
            instance = new JobManager(scheduler);
    }

    public static JobManager getInstance() {
        return instance;
    }

    public void scheduleJob(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        synchronized (lock) {
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }

    public void scheduleJob(JobDetail jobDetail, Set<Trigger> triggers, boolean replace) throws SchedulerException {
        synchronized (lock) {
            scheduler.scheduleJob(jobDetail, triggers, replace);
        }
    }

    public boolean unScheduleJob(String name, String group) throws SchedulerException {
        synchronized (lock) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(JobKey.jobKey(name, group));
            for (Trigger t : triggers) {
                if (!scheduler.unscheduleJob(t.getKey())) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean scheduleTrigger(Trigger trigger) throws SchedulerException {
        synchronized (lock) {
            return scheduler.scheduleJob(trigger) != null;
        }
    }

    public boolean unScheduleTrigger(Trigger trigger) throws SchedulerException {
        synchronized (lock) {
            return scheduler.unscheduleJob(trigger.getKey());
        }
    }

    public boolean checkExists(JobKey key) throws SchedulerException {
        synchronized (lock) {
            return scheduler.checkExists(key);
        }
    }

    private List<? extends Trigger> getTriggersOfJob(JobKey jobKey) throws SchedulerException {
        return scheduler.getTriggersOfJob(jobKey);
    }

    public List<? extends Trigger> getTriggersOfJob(String name, String group) throws SchedulerException {
        return getTriggersOfJob(JobKey.jobKey(name, group));
    }

    public boolean isScheduledJob(String name, String group) throws SchedulerException {
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(JobKey.jobKey(name, group));
        for (Trigger t : triggers) {
            if (t.getNextFireTime() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isScheduledTrigger(String name, String group) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(name, group));
        return trigger != null ? trigger.getNextFireTime() != null : false;
    }

    public boolean isPausedJob(String name, String group) throws SchedulerException {
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(JobKey.jobKey(name, group));
        for (Trigger t : triggers) {
            if (!scheduler.getTriggerState(t.getKey()).equals(Trigger.TriggerState.PAUSED)) {
                return false;
            }
        }
        return true;
    }

    public boolean isPausedTrigger(String name, String group) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(name, group));
        return scheduler.getTriggerState(trigger.getKey()).equals(Trigger.TriggerState.PAUSED);
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

    public void resumeTrigger(TriggerKey key) throws SchedulerException {
        scheduler.resumeTrigger(key);
    }

    public void pauseJob(String name, String group) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(name, group));
    }

    public void pauseJob(JobKey key) throws SchedulerException {
        scheduler.pauseJob(key);
    }

    public void pauseTrigger(TriggerKey key) throws SchedulerException {
        scheduler.pauseTrigger(key);
    }

    public void shutdown(boolean b) throws SchedulerException {
        if (scheduler != null)
            scheduler.shutdown(b);
    }

    public void resumeJob(JobKey jobKey) throws SchedulerException {
        scheduler.resumeJob(jobKey);
    }

    public void resumeJob(String name, String group) throws SchedulerException {
        this.resumeJob(JobKey.jobKey(name, group));
    }
}
