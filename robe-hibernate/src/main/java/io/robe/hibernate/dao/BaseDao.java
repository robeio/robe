package io.robe.hibernate.dao;


import com.google.common.base.Preconditions;
import io.dropwizard.hibernate.AbstractDAO;
import io.robe.common.service.headers.ResponseHeadersUtil;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.TypeReference;
import io.robe.hibernate.RobeHibernateBundle;
import io.robe.hibernate.criteria.api.Transformer;
import io.robe.hibernate.entity.RobeEntity;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Result;
import io.robe.hibernate.criteria.query.Query;
import io.robe.hibernate.criteria.hql.TransformerImpl;
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
    public Criteria<T> queryAllStrict(SearchModel search) {
        Query<T> query = new Query<>(new TransformerImpl<T>(this.currentSession()));
        return query.createCriteria(this.getEntityClass(), search);
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public Criteria<Map<String, Object>> queryAll(SearchModel search) {
        Transformer<Map<String, Object>> transformer = new TransformerImpl<>(this.currentSession(), Criteria.MAP_CLASS);
        Query<Map<String, Object>> query = new Query<>(transformer);
        return query.createCriteria(this.getEntityClass(), search);
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public <E> Criteria<E> queryAll(SearchModel search, Class<E> transformClass) {
        Query<E> query = new Query<>(new TransformerImpl<>(this.currentSession(), transformClass));
        return query.createCriteria(this.getEntityClass(), search);
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<T> findAllStrict(SearchModel search) {
        Result<T> resultPair = queryAllStrict(search).pairList();
        search.setTotalCount(resultPair.getTotalCount());
        ResponseHeadersUtil.addTotalCount(search);
        return resultPair.getList();
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<Map<String, Object>> findAll(SearchModel search) {
        Result<Map<String, Object>> resultPair = queryAll(search).pairList();
        search.setTotalCount(resultPair.getTotalCount());
        ResponseHeadersUtil.addTotalCount(search);
        return resultPair.getList();
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public <E> List<E> findAll(SearchModel search, Class<E> transformClass) {
        Result<E> resultPair = queryAll(search, transformClass).pairList();
        search.setTotalCount(resultPair.getTotalCount());
        ResponseHeadersUtil.addTotalCount(search);
        return resultPair.getList();
    }

    /**
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<T> findAllStrict() {
        return queryAllStrict(null).list();
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

