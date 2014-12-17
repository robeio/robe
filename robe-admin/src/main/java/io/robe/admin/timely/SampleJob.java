package io.robe.admin.timely;

import io.robe.quartz.common.TriggerInfo;
import io.robe.quartz.job.schedule.QJob;
import io.robe.quartz.job.schedule.QTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QJob(name = "SampleJob", description = "Sample Quartz Job for a demonstration.",
        triggers = {
//                @QTrigger(type = TriggerInfo.Type.CRON, name = "Minute", group = "TEST", cron = "1 * * * * ?"),
//                @QTrigger(type = TriggerInfo.Type.CRON, name = "Second", group = "TEST", cron = "* * * * * ?", startTime= 1418805997000L),
                @QTrigger(type = TriggerInfo.Type.SIMPLE, name = "Simple", group = "TEST", repeatCount = 5, repeatInterval = 2000, startTime= 1418805997000L ,endTime = 1418806057000L),
                @QTrigger(type = TriggerInfo.Type.ON_APP_START, name = "Start", group = "TEST")
        })
public class SampleJob implements org.quartz.Job {

    //TODO: Create sample projects and carry to it
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("TRIGGER: "+ context.getTrigger().getKey().getName()+" This is a Quartz Job   Next fire time : " + context.getNextFireTime());
    }
}
