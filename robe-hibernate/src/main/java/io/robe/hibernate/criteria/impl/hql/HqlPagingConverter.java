package io.robe.hibernate.criteria.impl.hql;

import io.robe.common.dto.Pair;
import io.robe.hibernate.criteria.api.criterion.RootCriteria;

import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Gets list data and total count by using {@link RootCriteria}
 * @param <T>
 */
public class HqlPagingConverter<T> extends HqlConverter<Pair<List<T>, Long>> {


    /**
     * @param session
     * @param transformClass
     */
    public HqlPagingConverter(Session session, Class<T> transformClass) {
        super(session, transformClass);
    }

    /**
     * converts {@link RootCriteria} to HQL
     * @param criteria
     * @return
     */
    @Override
    public Pair<List<T>, Long> convert(RootCriteria criteria) {

        Pair<List<T>, Long> response = new Pair<>();

        Pair<Pair<String, String>, Map<String, Object>> resultPair = HqlConverterUtil.listWithCount(criteria);
        Query listQuery = getSession().createQuery(resultPair.getLeft().getLeft());
        Query countQuery = getSession().createQuery(resultPair.getLeft().getRight());

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

        configureListQuery(criteria, listQuery);

        response.setLeft(listQuery.list());
        response.setRight((Long)countQuery.uniqueResult());

        return response;
    }


}
