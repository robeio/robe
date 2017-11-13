package io.robe.hibernate.criteria.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Result;
import io.robe.hibernate.criteria.api.Transformer;
import io.robe.hibernate.criteria.api.criterion.Restrictions;
import io.robe.hibernate.criteria.api.projection.Projections;
import io.robe.hibernate.criteria.api.query.QueryTestTools;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by kamilbukum on 24/01/2017.
 */
public class QueryTest extends QueryTestTools {
    @Test
    public void createCriteria() throws Exception {
        Session session = sessionFactory.openSession();
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setFields(new String[]{"name", "nickNames"});
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        Query<Map<String, Object>> query = new Query<>(new TransformerImpl<>(session, Criteria.MAP_CLASS));
        Result<Map<String, Object>> result = query.createCriteria(User.class, search).add(
                Restrictions.in("name", "deneme, deneme")
        ).pairList();
        System.out.println(result);
    }

    @Test
    public void createCriteria2() throws Exception {
        Session session = sessionFactory.openSession();
        SearchModel search = new SearchModel();
        Query<Map<String, Object>> query = new Query<>(new TransformerImpl<>(session, Criteria.MAP_CLASS));
        Result<Map<String, Object>> result = query.createCriteria(User.class, search)
                .setProjection(Projections
                        .projectionList()
                        .add(Projections.alias(Projections.groupProperty("active"), "active"))
                        .add(Projections.alias(Projections.count("active"), "count"))
                        .add(Projections.alias(Projections.avg("failCount"), "failCount"))
                )
                .pairList();
        System.out.println(result);
    }

}