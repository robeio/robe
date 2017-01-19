package io.robe.hibernate.query.api.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.query.HqlCriteriaTestTools;
import io.robe.hibernate.query.impl.hql.TransformerImpl;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;


/**
 * Created by kamilbukum on 16/01/2017.
 */
public class QueryTestTools extends HqlCriteriaTestTools {
    @Test
    public void createCriteria() throws Exception {
        Session session = sessionFactory.openSession();

        Query<User> query = new Query<>(new TransformerImpl<>(session));
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setFields(new String[]{"name", "active"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        List<User> userList = query.createCriteria(User.class, search).list();
        System.out.println(userList.size());
    }
}