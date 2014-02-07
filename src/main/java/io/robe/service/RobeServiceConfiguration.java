package io.robe.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import io.robe.mail.MailConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RobeServiceConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private DatabaseConfiguration database = new DatabaseConfiguration();

    @Valid
    @NotNull
    @JsonProperty
    private String entityPackage;

    @JsonProperty
    private MailConfiguration mail;


    public String getEntityPackage() {
        return entityPackage;
    }

    public DatabaseConfiguration getDatabaseConfiguration() {
        return database;
    }

    public MailConfiguration getMail() {
        return mail;
    }
}
