package io.robe.hibernate.criteria.api.criterion;

import io.robe.common.utils.Validations;

/**
 * Holds Query Operators as Enum
 */
public enum  Operators {
    /**
     * Holds unknown operators.
     */
    UNKNOWN(""),
    /**
     * Holds "equals" operator ( = ) to operate on all types
     */
    EQUALS("="),
    /**
     * Holds "not equals" operator ( != ) to operate on {@link String} or {@link Number} types
     */
    NOT_EQUALS("!="),
    /**
     * Holds "less than operator" ( < )  to operate on {@link String} or {@link Number} types
     */
    LESS_THAN("<"),
    /**
     * Holds "less than or equals operator" ( <= )  to operate on number types
     */
    LESS_OR_EQUALS_THAN("<="),
    /**
     * Holds "greater than operator" ( > ) to operate on number types
     */
    GREATER_THAN(">"),
    /**
     * Holds "greater than or equals operator" ( >= )  to operate on {@link Number} types
     */
    GREATER_OR_EQUALS_THAN(">="),
    /**
     * Holds "contains" ( ~= ) to operate on {@link String} types. In SQL its LIKE command
     */
    CONTAINS("~="),
    /**
     * Holds "in" ( |= ) operator to operate on @{@link io.robe.common.utils.Collections} types.
     */
    IN("|=");

    /**
     * Holds operator as {@link String}
     */
    private String op;

    /**
     *
     * @param op
     */
    Operators(String op) {
        this.op = op;
    }

    /**
     * gets Operator as {@link String}
     * @return
     */
    public String getOp() {
        return op;
    }

    /**
     * gets Operator as {@link Operators} enum type
     * @param op
     * @return
     */
    public static Operators getOperator(String op){
        if(Validations.isEmptyOrNull(op)) return Operators.UNKNOWN;
        for(Operators operators: Operators.values()) {
            if(operators.getOp().equals(op)) return operators;
        }
        return Operators.UNKNOWN;
    }
}