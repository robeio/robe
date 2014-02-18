package io.robe.hibernate;

import com.yammer.dropwizard.db.DatabaseConfiguration;

/**
 * Created by sinanselimoglu on 17/02/14.
 */
public class DBConfiguration extends DatabaseConfiguration {
    private String entityPackage;

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }
}
