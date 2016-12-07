package io.robe.hibernate.criteria.impl.hql;


import io.robe.hibernate.criteria.api.criterion.Restrictions;

/**
 * Generate Restrictions by filter operator
 */
public class HqlRestrictions implements Restrictions<String, String> {

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String eq(String key, String paramName) {
        return key + " = :" + paramName  ;
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public String isNull(String key) {
        return key + " IS NULL " ;
    }

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String ne(String key, String paramName) {
        return key + " != :" + paramName  ;
    }

    /**
     *
     * @param key
     * @return
     */
    @Override
    public String isNotNull(String key) {
        return key + " IS NOT NULL " ;
    }

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String lt(String key, String paramName) {
        return key + " < :" + paramName  ;
    }


    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String le(String key, String paramName) {
        return key + " <= :" + paramName  ;
    }

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String gt(String key, String paramName) {
        return key + " > :" + paramName  ;
    }

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String ge(String key, String paramName) {
        return key + " >= :" + paramName  ;
    }

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String ilike(String key, String paramName) {
        return key + " LIKE :" + paramName  ;
    }

    /**
     *
     * @param key
     * @param paramName
     * @return
     */
    @Override
    public String in(String key, String paramName) {
        return key + " IN (:" + paramName + ")"  ;
    }

    /**
     *
     * @param key
     * @param operator
     * @param paramName
     * @return
     */
    @Override
    public String custom(String key, String operator, String paramName) {
        return key + " " + operator + " :" + paramName  ;
    }

    /**
     *
     * @param key
     * @param op
     * @param value
     * @param paramName
     * @return
     */
    @Override
    public String filter(String key, String op, String value, String paramName) {
        return Restrictions.filter(this, key, op, value, paramName);
    }
}