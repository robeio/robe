package io.robe.quartz.common;

import org.quartz.Job;

import java.util.List;

public interface JobInfo {

    public String getName();

    public String getDescription();

    List<TriggerInfo> getTriggers();

    Class<? extends Job> getJobClass();


}
