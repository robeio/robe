package io.robe.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sinanselimoglu on 18/02/14.
 */
@OnApplicationStart
public class HelloManagedQuartz implements Job {

    private static Logger LOGGER = LoggerFactory.getLogger(HelloManagedQuartz.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("Hello Quartz Manager..!");
    }
}
