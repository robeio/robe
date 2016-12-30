package io.robe.admin;

import io.robe.quartz.RobeJob;
import io.robe.quartz.RobeTrigger;
import io.robe.quartz.info.TriggerInfo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by serayuzgur on 26/12/2016.
 */
@RobeJob(name = "Hello ANJOB", description = "A simple job says ANJOB", triggers = {
        @RobeTrigger(cron = "0/6 * * * * ?", name = "Every 6 seconds", group = "Sample", type = TriggerInfo.Type.CRON),
        @RobeTrigger(cron = "0/10 * * * * ?", name = "Every 10 seconds", group = "Sample", type = TriggerInfo.Type.CRON)
})
public class AnnJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("---" + jobExecutionContext.getTrigger().getKey().getName());
    }
}
