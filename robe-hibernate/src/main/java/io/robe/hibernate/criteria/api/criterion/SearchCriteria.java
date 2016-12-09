package io.robe.hibernate.criteria.api.criterion;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Abstract Class to hold criteria.
 */
public abstract class SearchCriteria {
    /**
     * Entity Alias
     */
    private final String alias;
    /**
     * Entity class Type
     */
    private final Class<?> entityClass;
    /**
     * Holds "AND" filters as key and value.
     * Key is field name in Entity
     * Value is an object which holds {@link Filter} information.
     */
    private final Map<String, Filter> filterMap = new LinkedHashMap<>();
    /**
     * Holds "OR" filters as key and value.
     * Key type is  {@link String}
     * Key is field name in Entity
     * Value type is {@link Filter}
     * Value is an object which holds filter information.
     */
    private final Map<String, Filter> orFilterMap = new LinkedHashMap<>();
    /**
     * Holds Join Criteria as key and value
     * Key type is  {@link String}
     * Key is alias for criteria
     * Value type is {@link JoinCriteria}
     * Value is {@link JoinCriteria} instance
     */
    private Map<String, JoinCriteria> criteriaMap;

    /**
     *
     * creates {@link RootCriteria}
     * @param alias
     * @param entityClass
     * @return
     */
    public static RootCriteria createQuery(String alias, Class<?> entityClass) {
        return new RootCriteria(alias, entityClass);
    }

    /**
     * @param alias
     * @param entityClass
     */
    protected SearchCriteria(String alias, Class<?> entityClass){
        this.alias = alias;
        this.entityClass = entityClass;
    }


    /**
     * Holds Join Criteria as key and value
     * Key type is  {@link String}
     * Key is alias for criteria
     * Value type is {@link JoinCriteria}
     * Value is {@link JoinCriteria} instance
     * @return
     */
    public Map<String, JoinCriteria> getCriteriaMap() {
        return criteriaMap;
    }

    /**
     * creates {@link JoinCriteria} and add it to Parent {@link SearchCriteria}
     * @param s
     * @return
     */
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
