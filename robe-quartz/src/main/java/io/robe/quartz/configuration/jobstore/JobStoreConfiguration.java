package io.robe.quartz.configuration.jobstore;

import java.util.Properties;

public class JobStoreConfiguration {
    private String className;

    private Properties properties;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
