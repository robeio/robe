package io.robe.hibernate.criteria.api.projection;

/**
 * Created by kamilbukum on 16/01/2017.
 */
public class EnhancedProjection implements Projection {
    private final String alias;
    private final Projection projection;

    public EnhancedProjection(Projection projection, String alias) {
        this.projection = projection;
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public Projection getProjection() {
        return projection;
    }

    @Override
    public boolean isGrouped() {
        return false;
    }

    @Override
    public String toString() {
        return "EnhancedProjection{" +
                "alias='" + alias + '\'' +
                ", projection=" + projection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnhancedProjection)) return false;

        EnhancedProjection that = (EnhancedProjection) o;

        if (getAlias() != null ? !getAlias().equals(that.getAlias()) : that.getAlias() != null) return false;
        return getProjection() != null ? getProjection().equals(that.getProjection()) : that.getProjection() == null;
    }

    @Override
    public int hashCode() {
        int result = getAlias() != null ? getAlias().hashCode() : 0;
        result = 31 * result + (getProjection() != null ? getProjection().hashCode() : 0);
        return result;
    }
}
