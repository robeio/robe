package io.robe.hibernate.dao;

import com.google.common.base.Preconditions;
import io.dropwizard.hibernate.AbstractDAO;
import io.robe.common.service.jersey.model.SearchModel;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic Dao Class which limits {@link io.dropwizard.hibernate.AbstractDAO} to take
 * type parameters which extends {@link io.robe.hibernate.entity.BaseEntity}
 *
 * @param <T> Type of the entity parameter.
 */
public class BaseDao<T extends BaseEntity> extends AbstractDAO<T> {

    private static final ConcurrentHashMap<String, Field[]> fieldCache = new ConcurrentHashMap<>();


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
    public List<T> findAll(SearchModel search) {
        Criteria criteria = buildCriteria(search);
        return criteria.list();
    }


    /**
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<T> findAll() {
        Criteria criteria = criteria();
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
     * {@inheritDoc}
     *
     * @param oid id of the Given Entity
     * @return returns the result.
     */
    @SuppressWarnings("unchecked")
    public T findById(Class<? extends BaseEntity> clazz, Serializable oid) {
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

    /**
     * Creates a criteria from the given search model.
     *
     * @param search model
     * @return criteria
     */
    protected final Criteria buildCriteria(SearchModel search) {

        Criteria criteria = criteria();
        if (search.getFields() != null && search.getFields().length != 0) {
            ProjectionList projectionList = Projections.projectionList();
            for (String fieldName : search.getFields()) {
                projectionList.add(Projections.property(fieldName), fieldName);
            }
            criteria.setProjection(projectionList);
        }
        if (search.getOffset() != null) {
            criteria.setFirstResult(search.getOffset());
        }
        if (search.getLimit() != null) {
            criteria.setMaxResults(search.getLimit());
        }
        if (search.getSort() != null && search.getSort().length != 0) {
            for (String fieldName : search.getSort()) {
                if (fieldName.startsWith(" ") || fieldName.startsWith("+")) {
                    criteria.addOrder(Order.asc(fieldName.substring(1)));
                } else if (fieldName.startsWith("-")) {
                    criteria.addOrder(Order.desc(fieldName.substring(1)));
                }
            }

        }
        if (search.getQ() != null && !search.getQ().isEmpty()) {
            Field[] fields = getCachedFields(this.getEntityClass());
            Criterion[] fieldLikes = new Criterion[fields.length];
            int i = 0;
            for (Field field : fields) {
                if (field.getType().equals(String.class))
                    fieldLikes[i++] = Restrictions.ilike(field.getName(), search.getQ(), MatchMode.ANYWHERE);
            }
            fieldLikes = Arrays.copyOf(fieldLikes, i);
            criteria.add(Restrictions.or(fieldLikes));
        }
        return criteria;
    }

    private Field[] getCachedFields(Class<T> entityClass) {
        if (!fieldCache.containsKey(entityClass.getName())) {
            fieldCache.put(entityClass.getName(), entityClass.getDeclaredFields());
        }
        return fieldCache.get(entityClass.getName());
    }
}

