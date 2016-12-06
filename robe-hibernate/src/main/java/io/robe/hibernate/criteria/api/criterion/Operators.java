package io.robe.hibernate.criteria.api.criterion;

import io.robe.common.utils.Validations;

/**
 * Created by kamilbukum on 23/11/16.
 */
public enum  Operators {
    UNKNOWN(""),
    EQUALS("="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    LESS_OR_EQUALS_THAN("<="),
    GREATER_THAN(">"),
    GREATER_OR_EQUALS_THAN(">="),
    CONTAINS("~="),
    IN("|=");

    private String op;

    Operators(String op) {
        this.op = op;
    }

    public String getOp() {
        return op;
    }

    public static Operators getOperator(String op){
        if(Validations.isEmptyOrNull(op)) return Operators.UNKNOWN;
        for(Operators operators: Operators.values()) {
            if(operators.getOp().equals(op)) return operators;
        }
        return Operators.UNKNOWN;
    }
}