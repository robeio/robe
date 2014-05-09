package io.robe.quartz;

import org.quartz.Job;

import java.util.List;

public interface QuartzJob {

    String getOid();

    String getSchedulerName();

    String getDescription();

    List<QuartzTrigger> getTriggers();

    Class<? extends Job> getClazz();

}
