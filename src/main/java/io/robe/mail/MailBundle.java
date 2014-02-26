package io.robe.mail;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import io.robe.service.RobeServiceConfiguration;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;

/**
 * Bundle class for mail implementation.
 */
public class MailBundle implements ConfiguredBundle<RobeServiceConfiguration> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MailBundle.class);


    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the service's {@link com.yammer.dropwizard.config.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
        if(configuration.getMailConfiguration() != null)
            MailSender.createInstance(configuration.getMailConfiguration());
        else
            LOGGER.warn("Bundle included but no configuration (mail) found at yml.");
    }

    /**
     * Initializes the service bootstrap.
     *
     * @param bootstrap the service bootstrap
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }
}
