package io.robe.hibernate.query.api.criteria.criterion;

import io.robe.hibernate.query.api.query.Operator;

/**
 * Created by kamilbukum on 16/01/2017.
 */
public class Restriction {
    private final Operator operator;
    private final Object value;
    private final String name;
    private final String valueAlias;

    public Restriction(Operator operator, String name) {
        this(operator, name, null,null);
    }

    public Restriction(Operator operator, String name, Object value, String valueAlias) {
        this.operator = operator;
        this.name = name;
        this.value = value;
        this.valueAlias = valueAlias;
    }

    public Operator getOperator() {
        return operator;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getValueAlias() {
        return valueAlias;
    }

    @Override
    public String toString() {
        return "Restriction{" +
                "operator=" + operator +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", valueAlias='" + valueAlias + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restriction)) return false;

        Restriction that = (Restriction) o;

        if (getOperator() != that.getOperator()) return false;
        if (getValue() != null ? !getValue().equals(that.getValue()) : that.getValue() != null) return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getValueAlias() != null ? getValueAlias().equals(that.getValueAlias()) : that.getValueAlias() == null;
    }

    @Override
    public int hashCode() {
        int result = getOperator() != null ? getOperator().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getValueAlias() != null ? getValueAlias().hashCode() : 0);
        return result;
    }
}
