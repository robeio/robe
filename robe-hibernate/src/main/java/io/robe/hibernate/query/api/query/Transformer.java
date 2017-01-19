package io.robe.hibernate.query.api.query;

import io.robe.hibernate.query.api.criteria.Criteria;
import io.robe.hibernate.query.api.criteria.Result;
import io.robe.hibernate.query.api.criteria.cache.EntityMetaFinder;
import io.robe.hibernate.query.api.criteria.criterion.Restrictions;

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
