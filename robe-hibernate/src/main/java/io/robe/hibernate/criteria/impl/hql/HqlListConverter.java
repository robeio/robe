package io.robe.hibernate.criteria.impl.hql;

import io.robe.common.dto.Pair;
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
 * Gets list data and total count by using {@link RootCriteria}
 */
public class HqlListConverter<T> extends HqlConverter<List<T>> {


    /**
     * @param session
     * @param transformClass
     */
    public HqlListConverter(Session session, Class<T> transformClass) {
        super(session, transformClass);
    }

    /**
     *
     * @param criteria
     * @return
     */
    @Override
    public List<T> convert(RootCriteria criteria) {

        Pair<String, Map<String, Object>> resultPair = HqlConverterUtil.list(criteria);

        Query query = getSession().createQuery(resultPair.getLeft());

        if(resultPair.getRight() != null) {
            for(Map.Entry<String, Object> entry: resultPair.getRight().entrySet()) {
                if(entry.getValue() instanceof Collection) {
                    query.setParameterList(entry.getKey(), (Collection) entry.getValue());
                } else {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        configureListQuery(criteria, query);
        return query.list();
    }
}
