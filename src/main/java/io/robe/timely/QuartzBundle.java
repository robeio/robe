package io.robe.timely;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import io.robe.service.RobeServiceConfiguration;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

/**
 * Created by sinanselimoglu on 14/02/14.
 */
public class QuartzBundle implements ConfiguredBundle<RobeServiceConfiguration> {
    @Override
    public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
        Timer timer = new Timer(configuration.getQuartzConfiguration(),configuration.getDatabaseConfiguration());
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }
}
