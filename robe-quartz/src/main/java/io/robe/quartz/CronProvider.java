package io.robe.quartz;

import org.quartz.Job;

public interface CronProvider {

    QuartzJob getQuartzJob(Class<? extends Job> clazz);

}
