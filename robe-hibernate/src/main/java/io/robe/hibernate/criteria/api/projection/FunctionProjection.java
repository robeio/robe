package io.robe.hibernate.criteria.api.projection;

/**
 * Created by kamilbukum on 16/01/2017.
 */
public class FunctionProjection implements Projection {
    private final String property;
    private final Type fnType;
    public FunctionProjection(String property, Type fnType) {
        this.property = property;
        this.fnType = fnType;
    }

    public String getProperty() {
        return property;
    }

    public Type getFnType() {
        return fnType;
    }

    @Override
    public boolean isGrouped() {
        return false;
    }

    public enum Type {
        COUNT, MIN, MAX , AVG, SUM
    }

    @Override
    public String toString() {
        return "FunctionProjection{" +
                "property='" + property + '\'' +
                ", fnType=" + fnType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FunctionProjection)) return false;

        FunctionProjection that = (FunctionProjection) o;

        if (getProperty() != null ? !getProperty().equals(that.getProperty()) : that.getProperty() != null)
            return false;
        return getFnType() == that.getFnType();
    }

    @Override
    public int hashCode() {
        int result = getProperty() != null ? getProperty().hashCode() : 0;
        result = 31 * result + (getFnType() != null ? getFnType().hashCode() : 0);
        return result;
    }
}
