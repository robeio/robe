package io.robe.hibernate.criteria.api.criterion;

import io.robe.common.utils.Validations;

/**
 * 
 * @param <K> key type
 * @param <R> output type
 */
public interface Restrictions <K , R> {
    // '=' equals operator
    R eq(K key, String paramName);
    R isNull(K key);
    // '!=' not equals operator
    R ne(K key, String paramName);
    R isNotNull(K key);
    // '<' less than operator
    R lt(K key, String paramName);
    // '<=' less or equals than operator
    R le(K key, String paramName);
    // '>' greater than operator
    R gt(K key, String paramName);
    // '>=' greater or equals than operator
    R ge(K key, String paramName);
    // '~=' contains than operator
    R ilike(K key, String paramName);
    // '|=' in list operator
    R in(K key, String paramName);
    // custom operator
    R custom(K key, String op, String paramName);

    static <R, K> R filter(Restrictions<K, R> restrictions, K key, String op, String value, String paramName) {
        Operators operator = Operators.getOperator(op);
        switch (operator) {
            case EQUALS:
                return Validations.isEmptyOrNull(value) ?
                        restrictions.isNull(key) :
                        restrictions.eq(key, paramName);
            case NOT_EQUALS:
                return Validations.isEmptyOrNull(value) ?
                        restrictions.isNotNull(key) :
                        restrictions.eq(key, paramName);
            case LESS_THAN:
                return restrictions.lt(key, paramName);
            case LESS_OR_EQUALS_THAN:
                return restrictions.le(key, paramName);
            case GREATER_THAN:
                return restrictions.gt(key, paramName);
            case GREATER_OR_EQUALS_THAN:
                return restrictions.ge(key, paramName);
            case CONTAINS:
                return restrictions.ilike(key, paramName);
            case IN:
                return restrictions.in(key, paramName);
            case UNKNOWN:
            default :
                return restrictions.custom(key, op, paramName);
        }
    }
}
