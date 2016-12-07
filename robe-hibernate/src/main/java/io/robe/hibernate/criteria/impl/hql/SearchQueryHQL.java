package io.robe.hibernate.criteria.impl.hql;

import io.robe.hibernate.criteria.api.CriteriaUtil;
import io.robe.common.dto.Pair;
import io.robe.hibernate.criteria.api.query.SearchQuery;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.TypeReference;
import org.hibernate.Session;

import java.util.List;
import java.util.Map;

/**
 * searchs data on Database by using HQL Language.
 */
public class SearchQueryHQL {

    /**
     *
     */
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {};

    /**
     * gets List of Data as "Entity" and "Total Count". Always returns element as Entity Type "<E>"
     * @param session Hibernate session
     * @param entityClass entityClass
     * @param search Search Model {@link SearchModel}
     * @param <E> Entity Type also return type
     * @return
     */
    public static <E> Pair<List<E>, Long> pairListStrict(Session session, Class<E> entityClass, SearchModel search){
        if(search.getFields() != null) {
            search.setFields(null);
        }
        HqlPagingConverter<E> hqlPagingConverter = new HqlPagingConverter<>(session, entityClass);
        Pair<List<E>, Long> resultPair = SearchQuery.createCriteria(entityClass, search).convert(hqlPagingConverter);
        return resultPair;
    }

    /**
     * gets List of Data As "Map<String,Object>" and "Total Count". Always returns element as "Map<String, Object>"
     * @param session Hibernate session
     * @param entityClass entityClass
     * @param search Search Model {@link SearchModel}
     * @return
     */
    public static Pair<List<Map<String, Object>>, Long> pairList(Session session, Class<?> entityClass, SearchModel search){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlPagingConverter<Map<String, Object>> hqlPagingConverter = new HqlPagingConverter<>(session, MAP_TYPE_REFERENCE.getClazz());
        Pair<List<Map<String, Object>>, Long> resultPair = SearchQuery.createCriteria(entityClass, search).convert(hqlPagingConverter);
        return resultPair;
    }

    /**
     * gets List of Data As "Transform Class Type" and "Total Count". Always returns element as "<T>"
     * @param session Hibernate session
     * @param entityClass entityClass
     * @param search Search Model {@link SearchModel}
     * @param transformClass output type
     * @param <T>
     * @return
     */
    public static <T> Pair<List<T>, Long> pairList(Session session, Class<?> entityClass, SearchModel search, Class<T> transformClass){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlPagingConverter<T> hqlPagingConverter = new HqlPagingConverter<>(session, transformClass);
        Pair<List<T>, Long> resultPair = SearchQuery.createCriteria(entityClass, search).convert(hqlPagingConverter);
        return resultPair;
    }

    /**
     * gets List of Data as "Entity". Always returns element as Entity Type "<E>"
     * @param session Hibernate session
     * @param entityClass entityClass
     * @param search Search Model {@link SearchModel}
     * @param <E>
     * @return
     */
    public static <E> List<E> listStrict(Session session, Class<E> entityClass, SearchModel search){
        if(search.getFields() != null) {
            search.setFields(null);
        }
        HqlListConverter<E> hqlListConverter = new HqlListConverter<>(session, entityClass);
        List<E> userList = SearchQuery.createCriteria(entityClass, search).convert(hqlListConverter);
        return userList;
    }

    /**
     * gets List of Data As "Map<String,Object>". Always returns element as "Map<String, Object>"
     * @param session Hibernate session
     * @param entityClass entityClass
     * @param search Search Model {@link SearchModel}
     * @return
     */
    public static List<Map<String, Object>> list(Session session, Class<?> entityClass,SearchModel search){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlListConverter<Map<String, Object>> hqlListConverter = new HqlListConverter<>(session, MAP_TYPE_REFERENCE.getClazz());
        List<Map<String, Object>> userList = SearchQuery.createCriteria(entityClass, search).convert(hqlListConverter);
        return userList;
    }

    /**
     * gets List of Data As "Transform Class Type". Always returns element as "<T>"
     * @param session Hibernate session
     * @param entityClass entityClass
     * @param search Search Model {@link SearchModel}
     * @param transformClass output type
     * @param <T>
     * @return
     */
    public static <T> List<T> list(Session session, Class<?> entityClass, SearchModel search, Class<T> transformClass){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlListConverter<T> hqlListConverter = new HqlListConverter<>(session, transformClass);
        List<T> userList = SearchQuery.createCriteria(entityClass, search).convert(hqlListConverter);
        return userList;
    }
}
