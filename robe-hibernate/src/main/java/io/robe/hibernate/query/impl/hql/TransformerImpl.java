package io.robe.hibernate.query.impl.hql;

import io.robe.common.dto.Pair;
import io.robe.hibernate.query.impl.hql.transformers.AliasToBeanResultTransformer;
import io.robe.hibernate.query.api.criteria.Criteria;
import io.robe.hibernate.query.api.criteria.Result;
import io.robe.hibernate.query.api.criteria.cache.EntityMetaFinder;
import io.robe.hibernate.query.api.query.Transformer;
import io.robe.hibernate.query.impl.hql.transformers.AliasToEntityMapResultTransformer;
import io.robe.hibernate.query.impl.hql.util.TransformUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class TransformerImpl<E> implements Transformer<E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerImpl.class);
    private static EntityMetaFinder finder = new EntityMetaFinderImpl();
    private Class<? extends E> transformClass;
    private Session session;

    public TransformerImpl(Session session) {
        this(session, null);
    }

    public TransformerImpl(Session session, Class<? extends E> transformClass) {
        this.session = session;
        this.transformClass = transformClass;
    }

    @Override
    public List<E> list(Criteria<E> criteria) {
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        String queryString = TransformUtil.generateDataQuery(criteria, parameterMap);

        System.out.println(queryString);
        Query query = session.createQuery(queryString);
        if(criteria.getLimit() != null) {
            query.setMaxResults(criteria.getLimit());
        }
        if(criteria.getOffset() != null) {
            query.setFirstResult(criteria.getOffset());
        }
        for(Map.Entry<String, Object> parameter: parameterMap.entrySet()) {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        setResultTransformer(criteria, query);
        return query.list();
    }


    @Override
    public Result<E> pairList(Criteria criteria) {
        Result<E> result  = new Result<>();
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        Pair<String, String> pairQuery = TransformUtil.generatePairResult(criteria, parameterMap);
        Query listQuery = session.createQuery(pairQuery.getLeft());
        setResultTransformer(criteria, listQuery);
        Query countQuery = session.createQuery(pairQuery.getRight());
        for(Map.Entry<String, Object> parameter: parameterMap.entrySet()) {
            listQuery.setParameter(parameter.getKey(), parameter.getValue());
            countQuery.setParameter(parameter.getKey(), parameter.getValue());
        }
        result.setList(listQuery.list());
        result.setTotalCount((long)countQuery.uniqueResult());
        return result;
    }

    @Override
    public Long count(Criteria<E> criteria) {
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        String hql = TransformUtil.generateCount(criteria, parameterMap);
        Query query = session.createQuery(hql);
        for(Map.Entry<String, Object> parameter: parameterMap.entrySet()) {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        setResultTransformer(criteria, query);
        return (long)query.uniqueResult();
    }

    @Override
    public Object uniqueResult(Criteria<E> criteria) {
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        String queryString = TransformUtil.generateDataQuery(criteria, parameterMap);
        System.out.println(queryString);
        Query query = session.createQuery(queryString);

        for(Map.Entry<String, Object> parameter: parameterMap.entrySet()) {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        setResultTransformer(criteria, query);
        return query.uniqueResult();
    }

    @Override
    public EntityMetaFinder getFinder() {
        return finder;
    }

    public void setResultTransformer(Criteria criteria, Query query){
        if(this.transformClass != null  && !this.transformClass.getName().equals(criteria.getEntityClass().getName())) {
            query.setResultTransformer(getTransformer(transformClass));
        }
    }

    /**
     * Holds ResultTransformers by given TransformerClass
     */
    private static Map<String, ResultTransformer> transformerMap = new ConcurrentHashMap<>();

    static {
        transformerMap.put(Map.class.getName(), AliasToEntityMapResultTransformer.INSTANCE);
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
}
