package io.robe.admin.timely;

import io.robe.quartz.annotations.Scheduled;
import org.apache.log4j.Logger;

import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * Created by sinanselimoglu on 28/03/14.
 */
@Scheduled(cron = "* * * * * ?",manager = Scheduled.Manager.REMOTE_EXTERNAL)
public class SendMail implements Job {

    private static Logger LOGGER = Logger.getLogger(SendMail.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if(new DateTime().getSecondOfDay()%3 == 0)
        LOGGER.info("Sending Mail. This is a Quartz Job and our time is      :   " + new Date().toString()+"   Next fire time : "+context.getNextFireTime());
        else if(new DateTime().getSecondOfDay()%3 == 1)
        LOGGER.debug("Sending Mail... This is a Quartz Job and our time is   :   " + new Date().toString()+"   Next fire time : "+context.getNextFireTime());
        else
        LOGGER.warn("Sending Mail...... This is a Quartz Job and our time is :   " + new Date().toString()+"   Next fire time : "+context.getNextFireTime());
    }
}
