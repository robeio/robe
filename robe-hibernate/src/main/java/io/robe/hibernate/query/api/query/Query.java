package io.robe.hibernate.query.api.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.Strings;
import io.robe.common.utils.Validations;
import io.robe.hibernate.query.api.criteria.*;
import io.robe.hibernate.query.api.criteria.cache.EntityMeta;
import io.robe.hibernate.query.api.criteria.cache.EntityMetaFinder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class Query<E> {
    /**
     * provides to transform criteria to query
     */
    private Transformer<E> transformer;

    /**
     *
     * @param transformer
     */
    public Query(Transformer<E> transformer) {
        this.transformer = transformer;
    }

    /**
     *
     * @param entityClass
     * @param search
     * @return
     */
    public Criteria<E> createCriteria(Class<?> entityClass, SearchModel search) {
        EntityMeta meta = CachedEntity.getEntityMeta(entityClass, transformer.getFinder());
        String alias = Strings.unCapitalizeFirstChar(entityClass.getSimpleName());
        Criteria<E> criteria = Criteria.createCriteria(alias, entityClass, transformer);

        if(search == null) {
            return criteria;
        }
        if(search.getQ() != null && search.getQ().length() > 0) {
            String[] queries = new String[]{search.getQ()};
            QueryUtility.configureQ(criteria, meta, transformer, queries, new HashSet<>(), null);
        }
        if(search.getFilter() != null && search.getFilter().length > 0) {
            QueryUtility.configureFilters(criteria, meta, transformer, search.getFilter());
        }

        if(search.getSort() != null && search.getSort().length > 0) {
            QueryUtility.configureSorts(criteria, meta, transformer, search.getSort());
        }

        if(search.getFields() != null && search.getFields().length > 0) {
            QueryUtility.configureFields(criteria, meta, transformer, search.getFields());
        }
        if(search.getLimit() != null) {
            criteria.setLimit(search.getLimit());
        }
        if(search.getOffset() != null) {
            criteria.setOffset(search.getOffset());
        }
        return criteria;
    }

    /**
     * Caches fields of Class
     */
    public static class CachedEntity {
        /**
         * Holds Cached Fields of Given Entity Class
         */
        private static final ConcurrentHashMap<String, EntityMeta> entityMetaMap = new ConcurrentHashMap<>();
        private static final ConcurrentHashMap<String, EntityMetaFinder> entityMetaFinder = new ConcurrentHashMap<>();

        private static EntityMeta upgradeCache(Class<?> entityClass, EntityMetaFinder finder){
            EntityMeta meta = finder.getEntityMeta(entityClass);
            entityMetaMap.put(entityClass.getName(), meta);
            entityMetaFinder.put(entityClass.getName(), finder);
            return meta;
        }

        public static EntityMeta getEntityMeta(Class<?> entityClass, EntityMetaFinder metaFinder) {
            EntityMeta meta = entityMetaMap.get(entityClass.getName());
            EntityMetaFinder finder = entityMetaFinder.get(entityClass.getName());
            if(meta == null || (finder != null && !finder.equals(metaFinder))) {
                meta = upgradeCache(entityClass, finder == null ? metaFinder: finder);
                if(meta == null) {
                    throw new RuntimeException("Entity Meta is null empty ! Please check the given finder that name is " + metaFinder.getClass().getName());
                }
                if(Validations.isEmptyOrNull(meta.getIdentityName())) {
                    throw new RuntimeException("Identity name not found in Entity Meta ! Please check the given entity that's name is " + entityClass.getName());
                }
            }
            return meta;
        }
    }
}
