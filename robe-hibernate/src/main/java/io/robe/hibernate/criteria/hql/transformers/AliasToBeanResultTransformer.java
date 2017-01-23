package io.robe.hibernate.criteria.hql.transformers;

import io.robe.hibernate.criteria.api.cache.EntityMeta;
import org.hibernate.HibernateException;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;


public class AliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AliasToBeanResultTransformer.class);
    private final Class<?> resultClass;
    private final EntityMeta transformMeta;
    public AliasToBeanResultTransformer(Class resultClass, EntityMeta transformMeta) {
        if ( resultClass == null ) {
            throw new IllegalArgumentException( "resultClass cannot be null" );
        }
        this.resultClass = resultClass;
        this.transformMeta = transformMeta;
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
                Field field = transformMeta.getFieldMap().get(name).getField();
                field.set(result, tuple[i]);
            }
        }
        catch ( InstantiationException | IllegalAccessException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }

        return result;
    }
}