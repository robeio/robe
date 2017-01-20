package io.robe.hibernate.query.impl.hql;

import io.robe.common.dto.Pair;
import io.robe.hibernate.query.api.criteria.cache.EntityMeta;
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

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class TransformerImpl<E> implements Transformer<E> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransformerImpl.class);
    private static EntityMetaFinder finder = new EntityMetaFinderImpl();
    private Class<? extends E> transformClass;
    private final TransformType transformType;
    private Session session;

    public TransformerImpl(Session session) {
        this(session, null);
    }

    public enum TransformType {
        ENTITY , MAP , DTO
    }

    public TransformerImpl(Session session, Class<? extends E> transformClass) {
        this.session = session;
        this.transformClass = transformClass;
        if(transformClass == null) {
           this.transformType = TransformType.ENTITY;
        } else if(transformClass.getName().equals(Map.class.getName())) {
            this.transformType = TransformType.MAP;
        } else {
            this.transformType = TransformType.DTO;
        }
    }

    public TransformType getTransformType() {
        return transformType;
    }

    @Override
    public List<E> list(Criteria<E> criteria) {
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        String queryString = TransformUtil.generateDataQuery(criteria, this,   parameterMap);

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
        setResultTransformer(query);
        return query.list();
    }



    @Override
    public Result<E> pairList(Criteria criteria) {
        Result<E> result  = new Result<>();
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        Pair<String, String> pairQuery = TransformUtil.generatePairResult(criteria, this, parameterMap);
        Query listQuery = session.createQuery(pairQuery.getLeft());
        if(criteria.getLimit() != null) {
            listQuery.setMaxResults(criteria.getLimit());
        }
        if(criteria.getOffset() != null) {
            listQuery.setFirstResult(criteria.getOffset());
        }
        setResultTransformer(listQuery);
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
        setResultTransformer(query);
        return (long)query.uniqueResult();
    }

    @Override
    public Object uniqueResult(Criteria<E> criteria) {
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        String queryString = TransformUtil.generateDataQuery(criteria, this, parameterMap);
        System.out.println(queryString);
        Query query = session.createQuery(queryString);

        for(Map.Entry<String, Object> parameter: parameterMap.entrySet()) {
            query.setParameter(parameter.getKey(), parameter.getValue());
        }
        setResultTransformer(query);
        return query.uniqueResult();
    }

    @Override
    public EntityMetaFinder getFinder() {
        return finder;
    }

    public void setResultTransformer(Query query){
        if(this.transformClass != null) {
            ResultTransformer transformer = getTransformer(transformClass, this.getTransformType());
            if(transformer != null) {
                query.setResultTransformer(transformer);
            }
        }
    }

    public static ResultTransformer getTransformer(Class<?> transformerClass, TransformType transformType){
        switch (transformType) {
            case MAP:
                return AliasToEntityMapResultTransformer.INSTANCE;
            case DTO:
                return new AliasToBeanResultTransformer(transformerClass);
        }
        return null;
    }
}
