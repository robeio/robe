package io.robe.hibernate.criteria.impl.hql;


import io.robe.hibernate.criteria.api.criterion.Restrictions;

/**
 * Created by kamilbukum on 23/11/16.
 */
public class HqlRestrictions implements Restrictions<String, String> {

    @Override
    public String eq(String key, String paramName) {
        return key + " = :" + paramName  ;
    }

    @Override
    public String isNull(String key) {
        return key + " IS NULL " ;
    }

    @Override
    public String ne(String key, String paramName) {
        return key + " != :" + paramName  ;
    }

    @Override
    public String isNotNull(String key) {
        return key + " IS NOT NULL " ;
    }

    @Override
    public String lt(String key, String paramName) {
        return key + " < :" + paramName  ;
    }

    @Override
    public String le(String key, String paramName) {
        return key + " <= :" + paramName  ;
    }

    @Override
    public String gt(String key, String paramName) {
        return key + " > :" + paramName  ;
    }

    @Override
    public String ge(String key, String paramName) {
        return key + " >= :" + paramName  ;
    }

    @Override
    public String ilike(String key, String paramName) {
        return key + " LIKE :" + paramName  ;
    }

    @Override
    public String in(String key, String paramName) {
        return key + " IN (:" + paramName + ")"  ;
    }

    @Override
    public String custom(String key, String operator, String paramName) {
        return key + " " + operator + " :" + paramName  ;
    }

    public String filter(String key, String op, String value, String paramName) {
        return Restrictions.filter(this, key, op, value, paramName);
    }
}