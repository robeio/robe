package io.robe.quartz;

import org.quartz.Job;

public interface CronProvider {

    public String getCron(Class<? extends Job> jobClass);
}
