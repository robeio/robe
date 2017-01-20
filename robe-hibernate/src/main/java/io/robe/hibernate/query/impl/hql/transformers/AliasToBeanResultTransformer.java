package io.robe.hibernate.query.impl.hql.transformers;

import io.robe.common.utils.reflection.Fields;
import org.hibernate.HibernateException;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;


public class AliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliasToBeanResultTransformer.class);
    private final Class<?> resultClass;
    private final Map<String, Field> fieldMap;
    public AliasToBeanResultTransformer(Class resultClass) {
        if ( resultClass == null ) {
            throw new IllegalArgumentException( "resultClass cannot be null" );
        }
        this.resultClass = resultClass;
        this.fieldMap = CacheFields.getCachedFields(resultClass);
    }

    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;
        try {
            result = resultClass.newInstance();
            for ( int i = 0; i < aliases.length; i++ ) {
                String name = aliases[i];
                Field field = this.fieldMap.get(name);
                field.set(result, tuple[i]);
            }
        }
        catch ( InstantiationException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        } catch ( IllegalAccessException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }

        return result;
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
                fieldCache.put(entityClass.getName(), Fields.getAllFieldsAsMap(entityClass, BEAN_FIELD_PREDICT));
            }
            return fieldCache.get(entityClass.getName());
        }
    }

    private static final Predicate<Field> BEAN_FIELD_PREDICT = (field) -> {
        if(field.isSynthetic()) return false;
        if((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) return false;
        if((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) return false;
        return true;
    };
}