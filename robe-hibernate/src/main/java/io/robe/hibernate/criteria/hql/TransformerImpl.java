package io.robe.hibernate.criteria.hql;

import io.robe.common.dto.Pair;
import io.robe.common.utils.reflection.Fields;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Result;
import io.robe.hibernate.criteria.api.Transformer;
import io.robe.hibernate.criteria.api.cache.EntityMetaFinder;
import io.robe.hibernate.criteria.hql.transformers.AliasToBeanResultTransformer;
import io.robe.hibernate.criteria.hql.transformers.AliasToEntityMapResultTransformer;
import io.robe.hibernate.criteria.hql.util.TransformUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * Created by kamilbukum on 10/01/2017.
 */
public class TransformerImpl<E> extends Transformer<E> {
    private static EntityMetaFinder finder = new EntityMetaFinderImpl();
    private Session session;

    public TransformerImpl(Session session) {
        this(session, null);
    }

    public TransformerImpl(Session session, Class<? extends E> transformClass) {
        super(transformClass, finder);
        this.session = session;
    }

    @Override
    public List<E> list(Criteria<E> criteria) {
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        String queryString = TransformUtil.generateDataQuery(criteria, this,   parameterMap);

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
        Pair<String, String> pairQuery = TransformUtil.generatePairResult(criteria, parameterMap);
        System.out.println(pairQuery.getLeft());
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

    private void setResultTransformer(Query query){
        switch (this.getTransformType()) {
            case MAP:
                query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
                break;
            case DTO:
                query.setResultTransformer(new AliasToBeanResultTransformer(this.getTransformClass(), this.getMeta()));
                break;
        }

    }

}
