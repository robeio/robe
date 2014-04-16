package io.robe.quartz;

import org.quartz.Job;

public interface CronProvider {

    public QuartzJob getQuartzJob(Class<? extends Job> clazz);

}
