package io.robe.hibernate.criteria.api.criterion;


import io.robe.hibernate.criteria.api.query.QueryConverter;

import java.util.*;

/**
 * Created by kamilbukum on 30/11/16.
 */
public class RootCriteria extends SearchCriteria {
    private Set<String> orderedSelects = new LinkedHashSet<>();
    private Map<String, SortType> orderedSortMap = new LinkedHashMap<>();
    /**
     * Starting index for the paged fetches.
     */
    private Integer offset;
    /**
     * Maximum number of results per page.
     */
    private Integer limit;


    public RootCriteria(String alias, Class<?> entityClass) {
        super(alias, entityClass);
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public <T> T convert(QueryConverter<T> converter) {
        return converter.convert(this);
    }

    public Set<String> getOrderedSelects() {
        return orderedSelects;
    }


    public Map<String, SortType> getOrderedSortMap() {
        return orderedSortMap;
    }
}

