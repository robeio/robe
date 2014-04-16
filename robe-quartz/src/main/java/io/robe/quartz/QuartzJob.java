package io.robe.quartz;

import org.quartz.Job;

import java.util.List;

public interface QuartzJob {

    public String getOid();

    public String getSchedulerName();

    public String getDescription();

    public List<QuartzTrigger> getTriggers();

    public Class<? extends Job> getClazz();

}
