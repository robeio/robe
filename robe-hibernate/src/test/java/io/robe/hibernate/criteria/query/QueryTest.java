package io.robe.hibernate.criteria.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Result;
import io.robe.hibernate.criteria.api.Transformer;
import io.robe.hibernate.criteria.api.query.QueryTestTools;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;

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
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        Query<User> query = new Query<>(new TransformerImpl<>(session, User.class));
        Result<User> result = query.createCriteria(User.class, search).pairList();
        System.out.println(result);
    }

}