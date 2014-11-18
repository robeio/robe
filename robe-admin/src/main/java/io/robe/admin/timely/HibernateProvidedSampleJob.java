package io.robe.admin.timely;

import io.robe.quartz.job.hibernate.ByHibernate;
import io.robe.quartz.job.schedule.ScheduledBy;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ScheduledBy(provider = ByHibernate.class)
public class HibernateProvidedSampleJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateProvidedSampleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        LOGGER.info("You will Make My DAy Day Day Day .....");
    }
}
