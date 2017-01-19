package io.robe.hibernate.query.impl.hql;

import io.robe.common.service.search.SearchFrom;
import io.robe.common.service.search.SearchIgnore;
import io.robe.common.utils.reflection.Fields;
import io.robe.hibernate.query.api.criteria.cache.EntityMeta;
import io.robe.hibernate.query.api.criteria.cache.EntityMetaFinder;
import io.robe.hibernate.query.api.criteria.cache.FieldMeta;
import io.robe.hibernate.query.api.criteria.cache.FieldReference;

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
        String identityName = fillFieldMetaMap(entityClass, fieldMetaMap);
        if(identityName == null) {
            throw new RuntimeException("@Id not found in " + entityClass.getName() + " class ! Id is required ! ");
        }
        return new EntityMeta(identityName, fieldMetaMap);
    }

    /**
     *
     * @param type
     * @param fieldMetaMap
     * @return
     */
    public static String fillFieldMetaMap(Class<?> type, Map<String, FieldMeta> fieldMetaMap) {
        String identityName = null;

        for(Field field: type.getDeclaredFields()) {
            if(!ENTITY_FIELD_PREDICATE.test(field)) continue;
            field.setAccessible(true);
            SearchIgnore ignore = field.getAnnotation(SearchIgnore.class);
            SearchFrom searchFrom = field.getAnnotation(SearchFrom.class);
            FieldMeta meta;
            if(searchFrom == null) { // this field hasn't any target
                meta = new FieldMeta(field.getType(), ignore != null);
            } else {
                FieldReference reference = new FieldReference(
                        searchFrom.entity(),
                        searchFrom.select(),
                        searchFrom.filter(),
                        searchFrom.id(),
                        field.getName()
                );
                meta = new FieldMeta(field.getType(), reference, ignore != null);
            }

            fieldMetaMap.put(field.getName(), meta);

            if(ENTITY_ID_FIELD_PREDICATE.test(field)) {
                identityName = field.getName();
            }
        }

        if(!type.getSuperclass().getName().equals(Object.class.getName())) {
            String superIdentityName = fillFieldMetaMap(type.getSuperclass(), fieldMetaMap);
            if(identityName == null) {
                identityName = superIdentityName;
            } else if(identityName != null && superIdentityName != null) {
                throw new RuntimeException("Found multiple id fields which has @Id annotation in " + type.getName() + " class!");
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
        if(field.getAnnotation(Transient.class) != null) return false;
        return true;
    };
}
