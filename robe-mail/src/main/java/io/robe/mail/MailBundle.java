package io.robe.mail;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import org.slf4j.Logger;

/**
 * Bundle class for mail implementation.
 */
public class MailBundle<T extends Configuration & HasMailConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MailBundle.class);

    private static MailSender mailSender = null;
    private static boolean isActive = false;

    public static boolean isIsActive() {
        return isActive;
    }

    private static synchronized void setIsActive(boolean isActive) {
        MailBundle.isActive = isActive;
    }

    public static MailSender getMailSender() {
        return mailSender;
    }

    public static synchronized void setMailSender(MailSender mailSender) {
        MailBundle.mailSender = mailSender;
    }



    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the io.robe.admin's {@link com.yammer.dropwizard.config.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(T configuration, Environment environment) throws Exception {
        if (configuration.getMailConfiguration() != null && configuration instanceof HasMailConfiguration && configuration instanceof Configuration) {
            setMailSender(new MailSender(configuration.getMailConfiguration()));
            setIsActive(true);
        } else {
            LOGGER.warn("Bundle included but no configuration (mail) found at yml.");
            setIsActive(false);
        }
    }

    /**
     * Initializes the io.robe.admin bootstrap.
     *
     * @param bootstrap the io.robe.admin bootstrap
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        LOGGER.info("Initializing MailBundle");
    }

}
