package io.robe.quartz.info;

import org.quartz.Job;

import java.util.List;

public interface JobInfo {

    String getName();

    String getDescription();

    List<TriggerInfo> getTriggers();

    void setTriggers(List<TriggerInfo> triggers);

    Class<? extends Job> getJobClass();

    Class<? extends JobInfoProvider> getProvider();
}
