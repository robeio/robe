package io.robe.hibernate.criteria.api.query;

import io.robe.hibernate.criteria.api.criterion.RootCriteria;

/**
 * Created by kamilbukum on 29/11/16.
 */
@FunctionalInterface
public interface QueryConverter<T> {
    T convert(RootCriteria query);
}
