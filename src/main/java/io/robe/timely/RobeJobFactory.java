package io.robe.timely;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

/**
 * Created by sinanselimoglu on 14/02/14.
 */
public class RobeJobFactory implements JobFactory {

    private static Injector injector;

    @Inject
    public RobeJobFactory(Injector injector) {
        this.injector = injector;
    }

    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) {
        JobDetail jobDetail = bundle.getJobDetail();
        Class<? extends Job> jobClass = jobDetail.getJobClass();
        return injector.getInstance(jobClass);
    }

}
