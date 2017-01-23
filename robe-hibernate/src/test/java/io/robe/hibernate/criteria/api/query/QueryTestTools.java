package io.robe.hibernate.criteria.api.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.criteria.query.Query;
import io.robe.hibernate.criteria.HqlCriteriaTestTools;
import io.robe.hibernate.criteria.hql.TransformerImpl;
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

        Query<User> query = new Query<>(new TransformerImpl<>(session, User.class));
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFields(new String[]{"name", "active", "roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        List<User> userList = query.createCriteria(User.class, search).list();
        System.out.println(userList.size());
    }
}