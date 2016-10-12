package io.robe.admin.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class SampleJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("Job is running");
    }
}
