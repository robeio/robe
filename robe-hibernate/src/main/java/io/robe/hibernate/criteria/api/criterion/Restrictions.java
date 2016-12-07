package io.robe.hibernate.criteria.api.criterion;

import io.robe.common.utils.Validations;

/**
 * 
 * @param <K> key type
 * @param <R> output type
 */
public interface Restrictions <K , R> {
    // '=' equals operator

    /**
     *  generates '=' equals
     * @param key
     * @param paramName
     * @return
     */
    R eq(K key, String paramName);

    /**
     *
     * @param key
     * @return
     */
    R isNull(K key);

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    // '!=' not equals operator
    R ne(K key, String paramName);

    /**
     *
     * @param key
     * @return
     */
    R isNotNull(K key);

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    // '<' less than operator
    R lt(K key, String paramName);

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    // '<=' less or equals than operator
    R le(K key, String paramName);

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    // '>' greater than operator
    R gt(K key, String paramName);

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    // '>=' greater or equals than operator
    R ge(K key, String paramName);

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    // '~=' contains than operator
    R ilike(K key, String paramName);

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    // '|=' in list operator
    R in(K key, String paramName);

    /**
     *
     * @param key
     * @param op
     * @param paramName
     * @return
     */
    // custom operator
    R custom(K key, String op, String paramName);

    /**
     *
     * @param restrictions
     * @param key
     * @param op
     * @param value
     * @param paramName
     * @param <R>
     * @param <K>
     * @return
     */
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

    /**
     *
     * @param key
     * @param op
     * @param value
     * @param paramName
     * @return
     */
    String filter(String key, String op, String value, String paramName);
}
