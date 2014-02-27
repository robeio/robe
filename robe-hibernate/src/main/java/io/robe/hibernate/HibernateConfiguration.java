package io.robe.hibernate;

import com.yammer.dropwizard.db.DatabaseConfiguration;

public class HibernateConfiguration extends DatabaseConfiguration {
    private String[] scanPackages;
    private String[] entities;

    public String[] getScanPackages() {
        return scanPackages;
    }

    public void setScanPackages(String[] scanPackages) {
        this.scanPackages = scanPackages;
    }

    public String[] getEntities() {
        return entities;
    }

    public void setEntities(String[] entities) {
        this.entities = entities;
    }
}
