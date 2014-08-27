package io.robe.quartz.configuration.jobstore;

import java.util.Properties;

public class RamJobStoreConfiguration extends JobStoreConfiguration {
    @Override
    public String getClassName() {
        return "org.quartz.simpl.RAMJobStore";
    }

    @Override
    public Properties getProperties() {
        return new Properties();
    }
}
