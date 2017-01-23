package io.robe.hibernate.criteria.api;

import io.robe.hibernate.criteria.api.cache.EntityMetaFinder;

import java.util.List;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public interface Transformer<E> {
    List<E> list(Criteria<E> criteria);
    Result<E> pairList(Criteria<E> criteria);
    Long count(Criteria<E> criteria);
    Object uniqueResult(Criteria<E> criteria);
    EntityMetaFinder getFinder();
}
