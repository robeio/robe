package io.robe.hibernate.criteria.impl.hql;

import io.robe.hibernate.criteria.api.ResultPair;
import io.robe.hibernate.criteria.api.criterion.RootCriteria;
import io.robe.hibernate.criteria.api.query.QueryConverter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 30/11/16.
 */
public class HqlListConverter<T> implements QueryConverter<List<T>> {
    private final Session session;
    private final Class<T> transformClass;

    public HqlListConverter(Session session, Class<T> transformClass) {
        this.session = session;
        this.transformClass = transformClass;
    }

    @Override
    public List<T> convert(RootCriteria criteria) {

        ResultPair<String, Map<String, Object>> resultPair = HqlConverterUtil.list(criteria);

        Query query = session.createQuery(resultPair.getLeft());

        if(resultPair.getRight() != null) {
            for(Map.Entry<String, Object> entry: resultPair.getRight().entrySet()) {
                if(entry.getValue() instanceof Collection) {
                    query.setParameterList(entry.getKey(), (Collection) entry.getValue());
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        if(transformClass.getName().equals(Map.class.getName())) {
            query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        } else if(!criteria.getEntityClass().equals(transformClass)) {
            query.setResultTransformer(Transformers.aliasToBean(transformClass));
        }

        if(criteria.getOffset() != null) {
            query.setFirstResult(criteria.getOffset());
        }
        if(criteria.getLimit() != null) {
            query.setMaxResults(criteria.getLimit());
        }
        return query.list();
    }
}
