package io.robe.hibernate.criteria.api.criterion;

/**
 * Created by kamilbukum on 02/12/16.
 */
public class Filter {
    private final String name;
    private final String operator;
    private final String value;
    public Filter(String name, String operator, String value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public String getValue() {
        return value;
    }
}
