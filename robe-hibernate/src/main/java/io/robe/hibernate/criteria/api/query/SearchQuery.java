package io.robe.hibernate.criteria.api.query;


import io.robe.hibernate.criteria.api.CriteriaUtil;
import io.robe.hibernate.criteria.api.criterion.RootCriteria;
import io.robe.hibernate.criteria.api.criterion.SearchCriteria;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.reflection.Fields;
import io.robe.common.utils.Strings;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * creates {@link RootCriteria} by given "Entity Class" and {@link SearchModel}
 */
public abstract class SearchQuery {

    /**
     *
     * @param entityClass
     * @param search
     * @return
     */
    public static RootCriteria createCriteria(Class<?> entityClass, SearchModel search){
        String alias = Strings.unCapitalizeFirstChar(entityClass.getSimpleName()) + 1;
        RootCriteria rootCriteria = SearchCriteria.createQuery(alias, entityClass);

        if(search.getQ() != null && search.getQ().length() > 0) {
            CriteriaUtil.configureQCriterions(rootCriteria, search.getQ());
        }
        if(search.getFilter() != null && search.getFilter().length > 0) {
            CriteriaUtil.configureFilters(rootCriteria, search.getFilter());
        }

        if(search.getSort() != null && search.getSort().length > 0) {
            CriteriaUtil.configureSortings(rootCriteria, search.getSort());
        }

        if(search.getFields() != null && search.getFields().length > 0) {
            CriteriaUtil.configureSelectFields(rootCriteria, search.getFields());
        }

        rootCriteria.setLimit(search.getLimit());
        rootCriteria.setOffset(search.getOffset());
        return rootCriteria;
    }

    /**
     * Caches fields of Class
     */
    public static class CacheFields {
        /**
         * Holds Cached Fields of Given Entity Class
         */
        private static final ConcurrentHashMap<String, Map<String, Field>> fieldCache = new ConcurrentHashMap<>();

        /**
         * Gets fields of given Entity Class. Caches at first time.
         * @param entityClass
         * @return
         */
        public static Map<String, Field> getCachedFields(Class<?> entityClass) {
            if (!fieldCache.containsKey(entityClass.getName())) {
                fieldCache.put(entityClass.getName(), Fields.getAllFieldsAsMap(entityClass));
            }
            return fieldCache.get(entityClass.getName());
        }
    }
}
