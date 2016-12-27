package io.robe.quartz.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Properties;

public class QuartzConfiguration {


    @NotNull
    @JsonProperty
    private String[] scanPackages;

    @NotNull
    @JsonProperty
    private Properties properties;


    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = Arrays.copyOf(scanPackages, scanPackages.length);
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
