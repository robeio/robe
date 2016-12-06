package io.robe.hibernate.dao;


import com.google.common.base.Preconditions;
import io.dropwizard.hibernate.AbstractDAO;
import io.robe.common.service.headers.ResponseHeadersUtil;
import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.RobeHibernateBundle;
import io.robe.hibernate.criteria.api.ResultPair;
import io.robe.hibernate.criteria.impl.hql.SearchQueryHQL;
import io.robe.hibernate.entity.RobeEntity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;

/**
 * Basic Dao Class which limits {@link io.dropwizard.hibernate.AbstractDAO} to take
 * type parameters which extends {@link io.robe.hibernate.entity.BaseEntity}
 *
 * @param <T> Type of the entity parameter.
 */
public class BaseDao<T extends RobeEntity> extends AbstractDAO<T> {

    @Inject
    RobeHibernateBundle bundle;

    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public BaseDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<T> findAllStrict(SearchModel search) {
        ResultPair<List<T>, Long> resultPair = SearchQueryHQL.pairListStrict(this.currentSession(), this.getEntityClass(), search);
        search.setTotalCount(resultPair.getRight());
        ResponseHeadersUtil.addTotalCount(search);
        return resultPair.getLeft();
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<Map<String, Object>> findAll(SearchModel search) {
        ResultPair<List<Map<String, Object>>, Long> resultPair = SearchQueryHQL.pairList(this.currentSession(), this.getEntityClass(), search);
        search.setTotalCount(resultPair.getRight());
        ResponseHeadersUtil.addTotalCount(search);
        return resultPair.getLeft();
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public <E> List<E> findAll(SearchModel search, Class<E> transformClass) {
        ResultPair<List<E>, Long> resultPair = SearchQueryHQL.pairList(this.currentSession(), this.getEntityClass(), search, transformClass);
        search.setTotalCount(resultPair.getRight());
        ResponseHeadersUtil.addTotalCount(search);
        return resultPair.getLeft();
    }

    /**
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<T> findAllStrict() {
        Criteria criteria = criteria();
        return list(criteria);
    }

    /**
     * {@inheritDoc}
     *
     * @param oid id of the desired Entity
     * @return returns the result.
     */
    public T findById(Serializable oid) {
        return get(oid);
    }

    /**
     * {@inheritDoc}
     *
     * @param oid id of the Given Entity
     * @return returns the result.
     */
    @SuppressWarnings("unchecked")
    public T findById(Class<? extends RobeEntity> clazz, Serializable oid) {
        return (T) currentSession().get(clazz, Preconditions.checkNotNull(oid));
    }

    /**
     * Create a record for the given entity instance.
     *
     * @param entity to record.
     * @return updated version of the instance.
     */
    public T create(T entity) {
        return persist(entity);
    }

    /**
     * Update a record for the given entity instance.
     *
     * @param entity to record.
     * @return updated version of the instance.
     */
    public T update(T entity) {
        return persist(entity);
    }

    /**
     * Delete a record for the given entity instance.
     *
     * @param entity to record.
     * @return updated version of the instance.
     */
    public T delete(T entity) {
        currentSession().delete(entity);
        return entity;
    }

    /**
     * Flush the session.
     */
    public void flush() {
        currentSession().flush();
    }

    /**
     * Merges the entity with the session.
     *
     * @param entity entity to merge
     * @return
     */
    @SuppressWarnings("unchecked")
    public T merge(T entity) {
        return (T) currentSession().merge(entity);
    }

    /**
     * Detached the entity from session by evict method.
     *
     * @param entity entity to detach
     * @return
     */
    public T detach(T entity) {
        currentSession().evict(entity);
        return entity;
    }
}

