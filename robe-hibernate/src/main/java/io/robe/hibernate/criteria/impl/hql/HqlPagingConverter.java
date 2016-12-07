package io.robe.hibernate.criteria.impl.hql;

import io.robe.common.dto.Pair;
import io.robe.common.utils.TypeReference;
import io.robe.hibernate.criteria.api.criterion.RootCriteria;

import io.robe.hibernate.criteria.api.query.QueryConverter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Gets list data and total count by using {@link RootCriteria}
 * @param <T>
 */
public class HqlPagingConverter<T> implements QueryConverter<Pair<List<T>, Long>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HqlPagingConverter.class);

    /**
     * Hibernate Session
     */
    private final Session session;
    /**
     * Transforming Class Type
     */
    private final Class<T> transformClass;

    /**
     *
     * @param session
     * @param transformClass
     */
    public HqlPagingConverter(Session session, Class<T> transformClass){
        this.session = session;
        this.transformClass = transformClass;
    }

    /**
     * converts {@link RootCriteria} to HQL
     * @param criteria
     * @return
     */
    @Override
    public Pair<List<T>, Long> convert(RootCriteria criteria) {

        Pair<Pair<String, String>, Map<String, Object>> resultPair = HqlConverterUtil.listWithCount(criteria);
        Query listQuery = session.createQuery(resultPair.getLeft().getLeft());
        Query countQuery = session.createQuery(resultPair.getLeft().getRight());

        if(resultPair.getRight() != null) {
            for(Map.Entry<String, Object> entry: resultPair.getRight().entrySet()) {
                if(entry.getValue() instanceof Collection) {
                    listQuery.setParameterList(entry.getKey(), (Collection) entry.getValue());
                    countQuery.setParameterList(entry.getKey(),(Collection) entry.getValue());
                } else {
                    listQuery.setParameter(entry.getKey(), entry.getValue());
                    countQuery.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        if(criteria.getOffset() != null) {
            listQuery.setFirstResult(criteria.getOffset());
        }
        if(criteria.getLimit() != null) {
            listQuery.setMaxResults(criteria.getLimit());
        }

        Pair<List<T>, Long> response = new Pair<>();

        List<T> tList ;
        if(transformClass.equals(Map.class)) {
            tList = listQuery.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
        } else if(!criteria.getEntityClass().equals(transformClass)) {
            // new AliasToBeanResultTransformer(transformClass)
            tList = listQuery.setResultTransformer(Transformers.aliasToBean(transformClass)).list();
        } else {
            tList = listQuery.list();
        }

        response.setLeft(tList);
        response.setRight((Long)countQuery.uniqueResult());
        return response;
    }
}
