package io.robe.hibernate.criteria.api.projection;

/**
 * Created by kamilbukum on 30/01/2017.
 */
public class ProjectionElements implements Projection {
    private final String property;
    public ProjectionElements(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public boolean isGrouped() {
        return false;
    }
}
