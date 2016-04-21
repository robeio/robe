package io.robe.hibernate.dao;

import com.google.common.base.Preconditions;
import io.dropwizard.hibernate.AbstractDAO;
import io.robe.common.service.headers.ResponseHeadersUtil;
import io.robe.common.service.search.SearchFrom;
import io.robe.common.service.search.SearchIgnore;
import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;

import javax.inject.Inject;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
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

    private static String[] parseFilterExp(String filter) {
        char[] chars = filter.toCharArray();
        char[] name = new char[chars.length];
        char[] op = new char[2];
        char[] value = new char[chars.length];
        int nIndex = 0;
        int oIndex = 0;
        int vIndex = 0;
        short part = 0;
        for (int i = 0; i < chars.length; i++) {
            switch (part) {
                case 0://Filling name
                    switch (chars[i]) {
                        case '=':
                        case '!':
                        case '<':
                        case '>':
                        case '~':
                            //Jump to operation
                            op[oIndex++] = chars[i];
                            part = 1;
                            break;
                        default:
                            name[nIndex++] = chars[i];
                    }
                    break;
                case 1://Filling op
                    switch (chars[i]) {
                        case '=':
                            op[oIndex++] = chars[i];
                            break;
                        default:
                            //Jump to value
                            value[vIndex++] = chars[i];
                            part = 2;
                    }
                    break;
                case 2://Filling value
                    value[vIndex++] = chars[i];
                    break;
            }
        }

        return new String[]{
                new String(name, 0, nIndex),
                new String(op, 0, oIndex),
                new String(value, 0, vIndex)};
    }

    /**
     * Returns modified list of the entities regarding to the search model.
     * {@inheritDoc}
     *
     * @return List of entities.
     */
    public List<T> findAll(SearchModel search) {
        List<T> list = buildCriteria(search).list();
        search.setLimit(null);
        search.setOffset(null);
        long totalCount = (Long) buildCriteria(search).setProjection(Projections.rowCount()).uniqueResult();
        search.setTotalCount(totalCount);
        ResponseHeadersUtil.addTotalCount(search);
        return list;
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
            criteria.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
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
            List<Criterion> fieldLikes = new ArrayList<>(fields.length);
            for (Field field : fields) {
                SearchFrom searchFrom = field.getAnnotation(SearchFrom.class);
                if (searchFrom != null) {
                    List<String> result = addRemoteMatchCriterias(searchFrom, search.getQ());
                    for (String id : result) {
                        fieldLikes.add(Restrictions.eq(field.getName(), id));
                    }
                } else if (field.getType().equals(String.class)) {
                    if (field.getAnnotation(SearchIgnore.class) == null) {
                        fieldLikes.add(Restrictions.ilike(field.getName(), search.getQ(), MatchMode.ANYWHERE));
                    }
                }
            }
            criteria.add(Restrictions.or(fieldLikes.toArray(new Criterion[]{})));
        }
        if (search.getFilter() != null) {
            Field[] fields = getCachedFields(this.getEntityClass());
            criteria.add(addFilterCriterias(fields, search.getFilter()));
        }


        return criteria;
    }

    private List<String> addRemoteMatchCriterias(SearchFrom from, String searchQ) {
        Criteria criteria = currentSession().createCriteria(from.entity());
        Field[] fields = getCachedFields(from.entity());
        Criterion[] fieldLikes = new Criterion[fields.length];
        int i = 0;
        for (Field field : fields) {
            if (field.getName().equals(from.target())) {
                if (field.getAnnotation(SearchIgnore.class) == null) {
                    fieldLikes[i++] = Restrictions.ilike(field.getName(), searchQ, MatchMode.ANYWHERE);
                }
            }
        }
        fieldLikes = Arrays.copyOf(fieldLikes, i);
        criteria.add(Restrictions.or(fieldLikes));
        criteria.setProjection(Projections.property(from.id()));
        return criteria.list();
    }

    public Conjunction addFilterCriterias(Field[] fields, String filterParam) {
        String[] filters = filterParam.split(",");
        Criterion[] fieldFilters = new Criterion[filters.length];
        int i = 0;
        for (String filter : filters) {
            String[] params = parseFilterExp(filter);
            Object value = null;
            for (Field field : fields) {
                if (field.getName().equals(params[0])) {
                    value = castValue(field, params[2]);
                    break;
                }
            }
            if (value == null)
                continue;
            switch (params[1]) {
                case "=":
                    fieldFilters[i++] = Restrictions.eq(params[0], value);
                    break;
                case "!=":
                    fieldFilters[i++] = Restrictions.ne(params[0], value);
                    break;
                case "<":
                    fieldFilters[i++] = Restrictions.lt(params[0], value);
                    break;
                case "<=":
                    fieldFilters[i++] = Restrictions.le(params[0], value);
                    break;
                case ">":
                    fieldFilters[i++] = Restrictions.gt(params[0], value);
                    break;
                case ">=":
                    fieldFilters[i++] = Restrictions.ge(params[0], value);
                    break;
                case "~=":
                    fieldFilters[i++] = Restrictions.ilike(params[0], params[2], MatchMode.ANYWHERE);
                    break;
            }
        }
        fieldFilters = Arrays.copyOf(fieldFilters, i);
        return Restrictions.and(fieldFilters);


    }

    private Object castValue(Field field, String value) {
        if (field.isEnumConstant())
            return value;

        switch (field.getType().getName()) {
            case "java.math.BigDecimal":
                return new BigDecimal(value);
            case "java.lang.Boolean":
            case "boolean":
                return Boolean.parseBoolean(value);
            case "java.lang.Double":
            case "double":
                return Double.parseDouble(value);
            case "java.lang.Integer":
            case "int":
                return Integer.parseInt(value);
            case "java.lang.Long":
            case "long":
                Long.parseLong(value);
            case "java.lang.String":
                return value;
            case "java.util.Date":
                return new Date(Long.parseLong(value));

        }
        return null;
    }

    private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));
        if (type.getSuperclass() != null) {
            fields = getAllFields(fields, type.getSuperclass());
        }
        return fields;
    }

    private Field[] getCachedFields(Class<?> entityClass) {
        if (!fieldCache.containsKey(entityClass.getName())) {
            List<Field> fields = new LinkedList<>();
            getAllFields(fields, entityClass);
            Field[] fieldArr = new Field[fields.size()];
            fields.toArray(fieldArr);

            fieldCache.put(entityClass.getName(), fieldArr);
        }
        return fieldCache.get(entityClass.getName());
    }
}

