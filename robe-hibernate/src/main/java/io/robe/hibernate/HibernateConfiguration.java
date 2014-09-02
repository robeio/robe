package io.robe.hibernate;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class HibernateConfiguration implements DatabaseConfiguration {
    private String[] scanPackages;
    private String[] entities;

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();


    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = Arrays.copyOf(scanPackages, scanPackages.length);
    }

    public String[] getEntities() {
        return entities;
    }

    public void setEntities(String[] entities) {
        this.entities = Arrays.copyOf(entities, entities.length);
    }

    @Override
    public DataSourceFactory getDataSourceFactory(Configuration configuration) {
        return database;
    }

}
