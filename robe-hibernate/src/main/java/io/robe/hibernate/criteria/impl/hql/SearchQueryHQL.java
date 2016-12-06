package io.robe.hibernate.criteria.impl.hql;

import io.robe.hibernate.criteria.api.CriteriaUtil;
import io.robe.hibernate.criteria.api.ResultPair;
import io.robe.hibernate.criteria.api.query.SearchQuery;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.TypeReference;
import org.hibernate.Session;
import org.reflections.Reflections;

import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 25/11/16.
 */
public class SearchQueryHQL {

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {};

    public static <E> ResultPair<List<E>, Long> pairListStrict(Session session, Class<E> entityClass, SearchModel search){
        if(search.getFields() != null) {
            search.setFields(null);
        }
        HqlPagingConverter<E> hqlPagingConverter = new HqlPagingConverter<>(session, entityClass);
        ResultPair<List<E>, Long> resultPair = SearchQuery.createCriteria(entityClass, search).convert(hqlPagingConverter);
        return resultPair;
    }

    public static ResultPair<List<Map<String, Object>>, Long> pairList(Session session, Class<?> entityClass,SearchModel search){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlPagingConverter<Map<String, Object>> hqlPagingConverter = new HqlPagingConverter<>(session, MAP_TYPE_REFERENCE.getClazz());
        ResultPair<List<Map<String, Object>>, Long> resultPair = SearchQuery.createCriteria(entityClass, search).convert(hqlPagingConverter);
        return resultPair;
    }

    public static <T> ResultPair<List<T>, Long> pairList(Session session, Class<?> entityClass, SearchModel search, Class<T> transformClass){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlPagingConverter<T> hqlPagingConverter = new HqlPagingConverter<>(session, transformClass);
        ResultPair<List<T>, Long> resultPair = SearchQuery.createCriteria(entityClass, search).convert(hqlPagingConverter);
        return resultPair;
    }

    public static <E> List<E> listStrict(Session session, Class<E> entityClass, SearchModel search){
        if(search.getFields() != null) {
            search.setFields(null);
        }
        HqlListConverter<E> hqlListConverter = new HqlListConverter<>(session, entityClass);
        List<E> userList = SearchQuery.createCriteria(entityClass, search).convert(hqlListConverter);
        return userList;
    }

    public static List<Map<String, Object>> list(Session session, Class<?> entityClass,SearchModel search){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlListConverter<Map<String, Object>> hqlListConverter = new HqlListConverter<>(session, MAP_TYPE_REFERENCE.getClazz());
        List<Map<String, Object>> userList = SearchQuery.createCriteria(entityClass, search).convert(hqlListConverter);
        return userList;
    }

    public static <T> List<T> list(Session session, Class<?> entityClass, SearchModel search, Class<T> transformClass){
        if(search.getFields() == null || search.getFields().length == 0) {
            search.setFields(CriteriaUtil.fromEntityFields2SearchFieldArray(entityClass));
        }
        HqlListConverter<T> hqlListConverter = new HqlListConverter<>(session, transformClass);
        List<T> userList = SearchQuery.createCriteria(entityClass, search).convert(hqlListConverter);
        return userList;
    }
}
