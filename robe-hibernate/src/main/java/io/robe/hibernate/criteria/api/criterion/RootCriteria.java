package io.robe.hibernate.criteria.api.criterion;


import io.robe.hibernate.criteria.api.query.QueryConverter;

import java.util.*;

/**
 * Holds Root Criteria information.
 */
public class RootCriteria extends SearchCriteria {
    /**
     * Holds select fields as {@link Set}.
     * Element Type is {@link String}
     */
    private Set<String> orderedSelects = new LinkedHashSet<>();
    /**
     * Holds sorts as key and value.
     * Key type is  {@link String}
     * Key is field name in Entity
     * Value type is {@link io.robe.hibernate.criteria.api.criterion.SearchCriteria.SortType}
     * Value is an enum object which describe ordering type. ( ASC , DESC )
     */
    private Map<String, SortType> orderedSortMap = new LinkedHashMap<>();
    /**
     * Starting index for the paged fetches.
     */
    private Integer offset;
    /**
     * Maximum number of results per page.
     */
    private Integer limit;

    /**
     *
     * @param alias
     * @param entityClass
     */
    public RootCriteria(String alias, Class<?> entityClass) {
        super(alias, entityClass);
    }

    /**
     * Sets starting index for the paged fetches.
     * @param offset
     */
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /**
     * Sets maximum number of results per page.
     * @param limit
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * Gets starting index for the paged fetches.
     * @return
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * Gets maximum number of results per page.
     * @return
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * called convert {@link java.lang.reflect.Method} of given converter which implements {@link QueryConverter}.
     * @param converter
     * @param <T>
     * @return
     */
    public <T> T convert(QueryConverter<T> converter) {
        return converter.convert(this);
    }

    /**
     * Gets select fields {@link Set}.
     * @return
     */
    public Set<String> getOrderedSelects() {
        return orderedSelects;
    }

    /**
     * Gets sorts {@link Map}
     * @return
     */
    public Map<String, SortType> getOrderedSortMap() {
        return orderedSortMap;
    }
}

