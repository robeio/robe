package io.robe.mail;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import io.robe.service.RobeServiceConfiguration;

public class MailBundle implements ConfiguredBundle<RobeServiceConfiguration> {


    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the service's {@link com.yammer.dropwizard.config.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
        MailSender.createInstance(configuration.getMail());
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
