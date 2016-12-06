package io.robe.hibernate.criteria.api.criterion;

import io.robe.hibernate.criteria.api.criterion.Filter;
import io.robe.hibernate.criteria.api.criterion.JoinCriteria;
import io.robe.hibernate.criteria.api.criterion.RootCriteria;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by kamilbukum on 28/11/16.
 */
public abstract class SearchCriteria {
    private final String alias;
    private final Class<?> entityClass;
    private final Map<String, String> selectFields = new LinkedHashMap<>();
    private final Map<String, Filter> filterMap = new LinkedHashMap<>();
    private final Map<String, Filter> orFilterMap = new LinkedHashMap<>();
    private final Map<String, SortType> sortingMap = new LinkedHashMap<>();
    private Map<String, JoinCriteria> criteriaMap;

    public static RootCriteria createQuery(String alias, Class<?> entityClass) {
        return new RootCriteria(alias, entityClass);
    }

    public SearchCriteria(String alias, Class<?> entityClass){
        this.alias = alias;
        this.entityClass = entityClass;
    }


    public Map<String, JoinCriteria> getCriteriaMap() {
        return criteriaMap;
    }

    public JoinCriteria getCriteria(String s) {
        return criteriaMap == null ? null: criteriaMap.get(s);
    }

    public JoinCriteria createJoinQuery(String alias, Class<?> entityClass, String idColumn, String referenceId) {
        JoinCriteria criteria = new JoinCriteria(this, alias, entityClass, idColumn, referenceId);
        if(criteriaMap == null) {
            criteriaMap = new LinkedHashMap<>();
        }
        criteriaMap.put(alias, criteria);
        return criteria;
    }

    public void addSelectField(String name) {
        addSelectField(name, name);
    }

    public void addSelectField(String name, String alias) {
        selectFields.putIfAbsent(alias, name);
    }

    public Map<String, String> getSelectFields() {
        return selectFields;
    }

    public void addSelectFields(Set<String> selectFieldsSet) {
        for(String selectField: selectFieldsSet) {
            addSelectField(selectField);
        }
    }

    public void addFilter(String filterName, String operator, String value) {
        Filter filter = new Filter(filterName, operator, value);
        filterMap.put(filterName, filter);
    }
    public void addOrFilter(String filterName, String operator, String value) {
        Filter filter = new Filter(filterName, operator, value);
        orFilterMap.put(filterName, filter);
    }

    public Map<String, Filter> getOrFilterMap() {
        return orFilterMap;
    }

    public Map<String, SortType> getSortingMap() {
        return sortingMap;
    }

    public enum SortType {
        ASC('+'), DESC('-');
        private char sort;

        SortType(char sort){
            this.sort = sort;
        }

        public static SortType getSortType(char sort) {
            for(SortType sortType: SortType.values()) {
                if(sortType.getSort() == sort) {
                    return sortType;
                }
            }
            return null;
        }

        public char getSort() {
            return sort;
        }
    }

    public Map<String, Filter> getFilters() {
        return filterMap;
    }

    public String getAlias() {
        return alias;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }
}
