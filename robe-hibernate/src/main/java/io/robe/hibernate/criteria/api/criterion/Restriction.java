package io.robe.hibernate.criteria.api.criterion;

import io.robe.hibernate.criteria.query.Operator;

/**
 * Created by kamilbukum on 16/01/2017.
 */
public class Restriction {
    private final Operator operator;
    private final Object value;
    private final String name;
    private String valueAlias;

    public Restriction(Operator operator, String name) {
        this(operator, name, null);
    }

    public Restriction(Operator operator, String name, Object value) {
        this.operator = operator;
        this.name = name;
        this.value = value;
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

    public void setValueAlias(String valueAlias) {
        this.valueAlias = valueAlias;
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
