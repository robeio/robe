package io.robe.quartz.job;

import org.quartz.Job;

public interface CronProvider {

    QuartzJob getQuartzJob(Class<? extends Job> clazz);

}
