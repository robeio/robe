package io.robe.admin.timely;

import io.robe.quartz.annotations.Scheduled;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Scheduled(cron = "",manager = Scheduled.Manager.REMOTE_EXTERNAL,description = "Make my day baby")
public class MakeMyDay implements Job {

    private static Logger LOGGER = Logger.getLogger(MakeMyDay.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
            LOGGER.info("You will Make My DAy Day Day Day .....");
    }
}
