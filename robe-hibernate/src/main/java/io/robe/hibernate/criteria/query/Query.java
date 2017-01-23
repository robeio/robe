package io.robe.hibernate.criteria.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.Strings;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Transformer;
/**
 * Created by kamilbukum on 10/01/2017.
 */
public class Query<E> {
    /**
     * provides to transform criteria to query
     */
    private Transformer<E> transformer;

    /**
     *
     * @param transformer
     */
    public Query(Transformer<E> transformer) {
        this.transformer = transformer;
    }

    /**
     *
     * @param entityClass
     * @param search
     * @return
     */
    public Criteria<E> createCriteria(Class<?> entityClass, SearchModel search) {
        String alias = Strings.unCapitalizeFirstChar(entityClass.getSimpleName());
        Criteria<E> criteria = Criteria.createCriteria(alias, entityClass, transformer);
        if(search == null) {
            return criteria;
        }

        if(search.getQ() != null && search.getQ().length() > 0) {
            String[] queries = new String[]{search.getQ()};
            QueryUtility.configureCriteriaByQ(criteria, queries);
        }

        if(search.getFilter() != null && search.getFilter().length > 0) {
            QueryUtility.configureFilters(criteria, search.getFilter());
        }

        if(search.getSort() != null && search.getSort().length > 0) {
            QueryUtility.configureSorts(criteria, search.getSort());
        }

        QueryUtility.configureSelectFields(criteria, search);

        if(search.getLimit() != null) {
            criteria.setLimit(search.getLimit());
        }
        if(search.getOffset() != null) {
            criteria.setOffset(search.getOffset());
        }
        return criteria;
    }
}
