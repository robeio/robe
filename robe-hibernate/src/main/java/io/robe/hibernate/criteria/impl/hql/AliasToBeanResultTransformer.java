package io.robe.hibernate.criteria.impl.hql;

import java.lang.reflect.Field;
import java.util.Map;

import io.robe.hibernate.criteria.api.query.SearchQuery;
import org.hibernate.HibernateException;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliasToBeanResultTransformer.class);

    private final Class resultClass;
    private Map<String, Field> fieldMap;

    public AliasToBeanResultTransformer(Class resultClass) {
        if ( resultClass == null ) {
            throw new IllegalArgumentException( "resultClass cannot be null" );
        }
        fieldMap = SearchQuery.CacheFields.getCachedFields(resultClass);
        this.resultClass = resultClass;
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
                Field field = fieldMap.get(name);

                if(field == null) {
                    LOGGER.error(name + " field not found in " + resultClass.getName() + " class ! ");
                    continue;
                }
                field.set(result,  tuple[i]);
            }
        }
        catch ( InstantiationException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        } catch ( IllegalAccessException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }

        return result;
    }
}