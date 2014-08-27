package io.robe.hibernate.dao;

import com.google.inject.Inject;
import io.dropwizard.hibernate.AbstractDAO;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Basic Dao Class which limits {@link io.dropwizard.hibernate.AbstractDAO} to take
 * type parameters which extends {@link io.robe.hibernate.entity.BaseEntity}
 *
 * @param <T> Type of the entity parameter.
 */
public class BaseDao<T extends BaseEntity> extends AbstractDAO<T> {

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
     * {@inheritDoc}
     *
     * @param t Type of the desired Entities.
     * @return List of entities.
     */
    public List<T> findAll(Class<T> t) {
        Criteria criteria = currentSession().createCriteria(t);
        return list(criteria);
    }


    /**
     * {@inheritDoc}
     *
     * @param oid id of the desired Entity
     * @return returns the result.
     */
    public T findById(String oid) {
        return get(oid);
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
