package io.robe.hibernate.query.api.criteria.cache;

/**
 * Created by kamilbukum on 12/01/2017.
 */
@FunctionalInterface
public interface EntityMetaFinder {
    EntityMeta getEntityMeta(Class<?> entityClass);
}
