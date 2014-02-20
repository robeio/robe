package io.robe.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import io.robe.hibernate.DBConfiguration;
import io.robe.mail.MailConfiguration;
import io.robe.quartz.QuartzConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RobeServiceConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private DBConfiguration database = new DBConfiguration();

    @JsonProperty
    private MailConfiguration mail;

    @Valid
    @NotNull
    @JsonProperty
    private QuartzConfiguration quartzConfiguration;

    public DBConfiguration getDatabaseConfiguration() {
        return database;
    }

    public MailConfiguration getMail() {
        return mail;
    }

    public QuartzConfiguration getQuartzConfiguration() {
        return quartzConfiguration;
    }
}
