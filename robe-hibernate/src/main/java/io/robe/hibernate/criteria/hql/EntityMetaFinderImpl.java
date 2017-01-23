package io.robe.hibernate.criteria.hql;

import io.robe.common.service.search.Relation;
import io.robe.common.service.search.SearchFrom;
import io.robe.common.service.search.SearchIgnore;
import io.robe.hibernate.criteria.api.cache.FieldReference;
import io.robe.hibernate.criteria.api.cache.EntityMeta;
import io.robe.hibernate.criteria.api.cache.EntityMetaFinder;
import io.robe.hibernate.criteria.api.cache.FieldMeta;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Created by kamilbukum on 12/01/2017.
 */
public class EntityMetaFinderImpl implements EntityMetaFinder {
    /**
     *
     * @param entityClass
     * @return
     */
    @Override
    public EntityMeta getEntityMeta(Class<?> entityClass) {
        Map<String, FieldMeta> fieldMetaMap = new LinkedHashMap<>();
        Map<String, String> relationMap = new LinkedHashMap<>();
        String identityName = fillFieldMetaMap(entityClass, fieldMetaMap, relationMap);
        return new EntityMeta(identityName, fieldMetaMap, relationMap);
    }

    /**
     *
     * @param type
     * @param fieldMetaMap
     * @return
     */
    public static String fillFieldMetaMap(Class<?> type, Map<String, FieldMeta> fieldMetaMap, Map<String, String> relationMap) {
        String identityName = null;

        for(Field field: type.getDeclaredFields()) {
            if(!ENTITY_FIELD_PREDICATE.test(field)) continue;
            field.setAccessible(true);
            Relation hasRelation = field.getAnnotation(Relation.class);
            Transient isTransient = field.getAnnotation(Transient.class);
            SearchIgnore ignore = field.getAnnotation(SearchIgnore.class);
            SearchFrom searchFrom = field.getAnnotation(SearchFrom.class);
            FieldMeta meta;
            if(searchFrom == null) { // this field hasn't any target
                meta = new FieldMeta(field, isTransient != null,ignore != null, hasRelation != null);
            } else {
                FieldReference reference = new FieldReference(
                        searchFrom.entity(),
                        searchFrom.select(),
                        searchFrom.filter(),
                        searchFrom.id(),
                        field.getName()
                );
                meta = new FieldMeta(field, reference, isTransient != null,ignore != null, hasRelation != null);
            }

            fieldMetaMap.put(field.getName(), meta);
            if(hasRelation != null) {
                meta.setRelationName(hasRelation.name());
                relationMap.put(hasRelation.name(), field.getName());
            }
            if(ENTITY_ID_FIELD_PREDICATE.test(field)) {
                identityName = field.getName();
            }
        }

        if(!type.getSuperclass().getName().equals(Object.class.getName())) {
            String superIdentityName = fillFieldMetaMap(type.getSuperclass(), fieldMetaMap, relationMap);
            if(identityName == null) {
                identityName = superIdentityName;
            }
        }
        return identityName;
    }


    private static final Predicate<Field> ENTITY_ID_FIELD_PREDICATE = (field) -> {
        if(field.getAnnotation(Id.class) != null) return true;
        return false;
    };

    private static final Predicate<Field> ENTITY_FIELD_PREDICATE = (field) -> {
        if(field.isSynthetic()) return false;
        if((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) return false;
        if((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) return false;
        return true;
    };
}
