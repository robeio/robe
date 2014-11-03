package io.robe.admin.configuration;

import io.robe.guice.GuiceConfiguration;

public class RobeConfigurationManager {
    private static RobeConfigurationManager ourInstance = new RobeConfigurationManager();
    private GuiceConfiguration guiceConfiguration;

    private RobeConfigurationManager() {
    }

    public static RobeConfigurationManager getInstance() {
        return ourInstance;
    }

    public GuiceConfiguration getGuiceConfiguration() {
        return guiceConfiguration;
    }

    public void setGuiceConfiguration(GuiceConfiguration guiceConfiguration) {
        this.guiceConfiguration = guiceConfiguration;
    }
}
