package io.robe.hibernate.criteria.api.cache;

/**
 * Created by kamilbukum on 12/01/2017.
 */
@FunctionalInterface
public interface EntityMetaFinder {
    EntityMeta getEntityMeta(Class<?> entityClass);
}
