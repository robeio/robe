package io.robe.hibernate.criteria.api.criterion;

/**
 *  Holds filter information
 */
public class Filter {
    /**
     * holds name of filter example.name
     */
    private final String name;
    /**
     * holds ({@link Operators}) of filter
     */
    private final String operator;
    /**
     * holds value of filter
     */
    private final String value;

    /**
     *
     * @param name
     * @param operator
     * @param value
     */
    public Filter(String name, String operator, String value) {
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    /**
     * get name of filter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * gets operator of filter
     */
    public String getOperator() {
        return operator;
    }

    /**
     * gets value of filter
     * @return
     */
    public String getValue() {
        return value;
    }
}
