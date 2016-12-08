package io.robe.hibernate.criteria.impl.hql;

import io.robe.hibernate.criteria.api.criterion.RootCriteria;
import io.robe.hibernate.criteria.api.query.QueryConverter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kamilbukum on 07/12/16.
 */
public abstract class HqlConverter<T> implements QueryConverter<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HqlConverter.class);
    /**
     * Transforming Class Type
     */
    private final Class<?> transformClass;
    /**
     * Hibernate Session
     */
    private final Session session;

    /**
     *
     * @param session
     * @param transformClass
     */
    public HqlConverter(Session session, Class<?> transformClass) {
        this.session = session;
        this.transformClass = transformClass;
    }

    /**
     * Holds ResultTransformers by given TransformerClass
     */
    private static Map<String, ResultTransformer> transformerMap = new ConcurrentHashMap<>();

    static {
        transformerMap.put(Map.class.getName(), Criteria.ALIAS_TO_ENTITY_MAP);
    }

    public static ResultTransformer getTransformer(Class<?> transformerClass){
        ResultTransformer transformer = transformerMap.get(transformerClass.getName());
        if(transformer == null) {
            transformer = new AliasToBeanResultTransformer(transformerClass);
            transformerMap.put(transformerClass.getName(), transformer);
            LOGGER.info("Created ResultTransformer for " +  transformerClass);
        }
        return transformer;
    }


    public void configureListQuery(RootCriteria criteria, Query listQuery){
        if(criteria.getOffset() != null) {
            listQuery.setFirstResult(criteria.getOffset());
        }
        if(criteria.getLimit() != null) {
            listQuery.setMaxResults(criteria.getLimit());
        }
        if(!criteria.getEntityClass().equals(getTransformClass())) {
            listQuery.setResultTransformer(getTransformer(getTransformClass()));
        }
    }

    public Session getSession() {
        return session;
    }

    public Class<?> getTransformClass() {
        return transformClass;
    }
}
