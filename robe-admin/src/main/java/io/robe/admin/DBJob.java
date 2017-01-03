package io.robe.admin;

import io.robe.admin.quartz.HibernateJobInfoProvider;
import io.robe.quartz.RobeJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by serayuzgur on 26/12/2016.
 */
@RobeJob(name = "DBJOB", description = "A simple job says DB", provider = HibernateJobInfoProvider.class)
public class DBJob implements Job {
    private static final Logger LOGGER = LoggerFactory.getLogger(DBJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("-------------------------");
        LOGGER.info("DB!!!" + jobExecutionContext.getTrigger().getKey().getName());
        LOGGER.info("-------------------------");
    }
}
