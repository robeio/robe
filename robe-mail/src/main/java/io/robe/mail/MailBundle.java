package io.robe.mail;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;

/**
 * Bundle class for mail implementation.
 */
public class MailBundle<T extends Configuration & HasMailConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MailBundle.class);

    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the io.robe.admin's {@link io.dropwizard.setup.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(T configuration, Environment environment) throws Exception {
        LOGGER.info("\n------------------------\n------Mail Bundle-------\n------------------------");
        if (configuration.getMailConfiguration() != null && configuration instanceof HasMailConfiguration && configuration instanceof Configuration) {
	        MailManager.setSender(new MailSender(configuration.getMailConfiguration()));
        } else {
            LOGGER.warn("Bundle included but no configuration (mail) found at yml.");
        }
    }


    /**
     * Initializes the io.robe.admin bootstrap.
     *
     * @param bootstrap the io.robe.admin bootstrap
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

}
