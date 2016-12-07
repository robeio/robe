package io.robe.hibernate.criteria.api.query;

import io.robe.hibernate.criteria.api.criterion.RootCriteria;

/**
 * Gets Data by using {@link RootCriteria}
 */
@FunctionalInterface
public interface QueryConverter<T> {
    /**
     *
     * @param query
     * @return
     */
    T convert(RootCriteria query);
}
