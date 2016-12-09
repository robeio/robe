package io.robe.hibernate.criteria.impl.hql;

import io.robe.common.dto.Pair;
import io.robe.common.utils.Strings;
import io.robe.hibernate.criteria.api.query.SearchQuery;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.TypeReference;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        fillSelectFieldsIfNotExist(entityClass, entityClass, search);
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
        fillSelectFieldsIfNotExist(entityClass, transformClass, search);
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
        fillSelectFieldsIfNotExist(entityClass, entityClass, search);
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
        fillSelectFieldsIfNotExist(entityClass, transformClass, search);
        HqlListConverter<T> hqlListConverter = new HqlListConverter<>(session, transformClass);
        List<T> userList = SearchQuery.createCriteria(entityClass, search).convert(hqlListConverter);
        return userList;
    }

    public static void fillSelectFieldsIfNotExist(Class<?> entityClass, Class<?> transformClass, SearchModel search){
        if(search.getFields() == null || search.getFields().length == 0) {
            if(entityClass.equals(transformClass)) {
                Map<String, Field> fieldMap = SearchQuery.CacheFields.getCachedFields(transformClass);
                if(fieldMap != null && fieldMap.size() > 0) {
                    search.setFields(fieldMap.keySet().toArray(new String[]{}));
                }
            } else { // Transform Class
                Map<String, Field> fieldMap = SearchQuery.CacheFields.getCachedFields(transformClass);

                Map<String, Field> entityFieldMap = SearchQuery.CacheFields.getCachedFields(entityClass);

                Set<String> selectSet = new HashSet<>();

                for(Map.Entry<String, Field> transformFieldEntry: fieldMap.entrySet()) {
                    if(entityFieldMap.keySet().contains(transformFieldEntry.getKey())) {
                        selectSet.add(transformFieldEntry.getKey());
                        continue;
                    }

                    for(String entityFieldName: entityFieldMap.keySet()) {
                        if(transformFieldEntry.getKey().startsWith(entityFieldName)) {
                           String transformField =  entityFieldName + "." + Strings.unCapitalizeFirstChar(transformFieldEntry.getKey().substring(entityFieldName.length()));
                           selectSet.add(transformField);
                        }
                    }
                }
                search.setFields(selectSet.toArray(new String[]{}));
            }
        }
    }
}
