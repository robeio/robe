package io.robe.hibernate;

import com.yammer.dropwizard.db.DatabaseConfiguration;

import java.util.Arrays;

public class HibernateConfiguration extends DatabaseConfiguration {
    private String[] scanPackages;
    private String[] entities;

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
}
