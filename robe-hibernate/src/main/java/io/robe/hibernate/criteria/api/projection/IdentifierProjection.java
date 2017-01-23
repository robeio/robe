package io.robe.hibernate.criteria.api.projection;

/**
 * Created by kamilbukum on 16/01/2017.
 */
public class IdentifierProjection implements Projection {
    @Override
    public boolean isGrouped() {
        return false;
    }

    @Override
    public String toString() {
        return "IdentifierProjection{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdentifierProjection)) return false;
        return true;
    }
}
