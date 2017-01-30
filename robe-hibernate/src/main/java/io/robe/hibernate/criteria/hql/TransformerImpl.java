package io.robe.hibernate.criteria.hql;

import io.robe.common.dto.BooleanHolder;
import io.robe.common.dto.Pair;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Result;
import io.robe.hibernate.criteria.api.Transformer;
import io.robe.hibernate.criteria.api.cache.EntityMetaFinder;
import io.robe.hibernate.criteria.hql.transformers.AliasToBeanResultTransformer;
import io.robe.hibernate.criteria.hql.transformers.AliasToEntityMapResultTransformer;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.*;

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
        Pair<String, Map<String, Object>> pair = TransformerUtil.query(criteria);

        Query query = session.createQuery(pair.getLeft());
        if(criteria.getLimit() != null) {
            query.setMaxResults(criteria.getLimit());
        }
        if(criteria.getOffset() != null) {
            query.setFirstResult(criteria.getOffset());
        }
        for(Map.Entry<String, Object> parameter: pair.getRight().entrySet()) {
            setParameter(query, parameter.getKey(), parameter.getValue());
        }
        setResultTransformer(query);
        return query.list();
    }



    @Override
    public Result<E> pairList(Criteria<E> criteria) {
        Result<E> result  = new Result<>();
        BooleanHolder groupBy = new BooleanHolder(false);
        Pair<String, Pair<String, Map<String, Object>>> pair = TransformerUtil.pairList(criteria, groupBy);
        System.out.println(pair.getLeft());
        Query listQuery = session.createQuery(pair.getLeft());
        if(criteria.getLimit() != null) {
            listQuery.setMaxResults(criteria.getLimit());
        }
        if(criteria.getOffset() != null) {
            listQuery.setFirstResult(criteria.getOffset());
        }
        setResultTransformer(listQuery);
        Query countQuery = session.createQuery(pair.getRight().getLeft());
        for(Map.Entry<String, Object> parameter: pair.getRight().getRight().entrySet()) {
            setParameter(listQuery, parameter.getKey(), parameter.getValue());
            setParameter(countQuery, parameter.getKey(), parameter.getValue());
        }
        result.setList(listQuery.list());
        result.setTotalCount(groupBy.is() ? countQuery.list().size(): (long)countQuery.uniqueResult());
        return result;
    }

    public void setParameter(Query query, String key, Object value) {
        if(value instanceof Collection) {
            query.setParameterList(key, (Collection) value);
        } else {
            query.setParameter(key, value);
        }
    }

    @Override
    public Long count(Criteria<E> criteria) {
        BooleanHolder groupBy = new BooleanHolder(false);
        Pair<String, Map<String, Object>> pair = TransformerUtil.count(criteria, groupBy);
        System.out.println(pair.getLeft());
        Query query = session.createQuery(pair.getLeft());
        for(Map.Entry<String, Object> parameter: pair.getRight().entrySet()) {
            setParameter(query, parameter.getKey(), parameter.getValue());
        }
        setResultTransformer(query);
        return groupBy.is() ? query.list().size(): (long)query.uniqueResult();
    }

    @Override
    public Object uniqueResult(Criteria<E> criteria) {
        Pair<String, Map<String, Object>> pair = TransformerUtil.query(criteria);
        Query query = session.createQuery(pair.getLeft());

        for(Map.Entry<String, Object> parameter: pair.getRight().entrySet()) {
            setParameter(query, parameter.getKey(), parameter.getValue());
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
