package io.robe.admin.timely;

import io.robe.quartz.annotations.Scheduled;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

@Scheduled(cron = "* * * * 1 ?", startTime = "12.21.1985 12:21:11")
public class AnnotationProvidedSampleJob implements Job {

    private static final Logger LOGGER = Logger.getLogger(AnnotationProvidedSampleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Sending Mail. This is a Quartz Job and our time is      :   " + new Date().toString() + "   Next fire time : " + context.getNextFireTime());
    }
}
