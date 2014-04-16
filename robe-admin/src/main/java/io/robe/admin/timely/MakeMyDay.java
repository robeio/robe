package io.robe.admin.timely;

import io.robe.quartz.annotations.ScheduledBy;
import io.robe.quartz.hibernate.ByHibernate;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@ScheduledBy(provider = ByHibernate.class)
public class MakeMyDay implements Job {

    private static Logger LOGGER = Logger.getLogger(MakeMyDay.class);
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
            LOGGER.info("You will Make My DAy Day Day Day .....");
    }
}
