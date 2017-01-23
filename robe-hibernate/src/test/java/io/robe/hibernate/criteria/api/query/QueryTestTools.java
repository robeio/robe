package io.robe.hibernate.criteria.api.query;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Result;
import io.robe.hibernate.criteria.query.Query;
import io.robe.hibernate.criteria.HqlCriteriaTestTools;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.User;
import io.robe.hibernate.test.entity.UserDTO;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;
import java.util.Map;


/**
 * Created by kamilbukum on 16/01/2017.
 */
public class QueryTestTools extends HqlCriteriaTestTools {

    @Test
    public void createCriteriaForEntity() throws Exception {
        Session session = sessionFactory.openSession();
        Query<User> query = new Query<>(new TransformerImpl<>(session));
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        List<User> userList = query.createCriteria(User.class, search).list();
        System.out.println(userList.size());
    }

    @Test
    public void createCriteriaForDTOEntiry() throws Exception {
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

    @Test
    public void createCriteriaForDTO() throws Exception {
        Session session = sessionFactory.openSession();
        Query<UserDTO> query = new Query<>(new TransformerImpl<>(session, UserDTO.class));
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFields(new String[]{"name", "active", "roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        List<UserDTO> userList = query.createCriteria(User.class, search).list();
        System.out.println(userList.size());
    }


    @Test
    public void createCriterForMap() throws Exception {
        Session session = sessionFactory.openSession();
        Query<Map<String, Object>> query = new Query<>(new TransformerImpl<>(session, Criteria.MAP_CLASS));
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFields(new String[]{"name", "active", "roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        List<Map<String, Object>> userList = query.createCriteria(User.class, search).list();
        System.out.println(userList.size());
    }


    @Test
    public void pairListStrict() {
        Session session = sessionFactory.openSession();
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}, {"roleOid.name", "=", "Example First Role"}});
        Query<User> query = new Query<>(new TransformerImpl<>(session));
        Result<User> result = query.createCriteria(User.class, search).pairList();
        System.out.println(result);
    }

    @Test
    public void pairListDTOEntity() {
        Session session = sessionFactory.openSession();
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        Query<User> query = new Query<>(new TransformerImpl<>(session, User.class));
        Result<User> result = query.createCriteria(User.class, search).pairList();
        System.out.println(result);
    }
    @Test
    public void pairListDTO() {
        Session session = sessionFactory.openSession();
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        Query<UserDTO> query = new Query<>(new TransformerImpl<>(session, UserDTO.class));
        Result<UserDTO> result = query.createCriteria(User.class, search).pairList();
        System.out.println(result);
    }

    @Test
    public void pairListMap() {
        Session session = sessionFactory.openSession();
        SearchModel search = new SearchModel();
        search.setQ("Example First Role");
        search.setSort(new String[]{"-name", "+roleOid.name"});
        search.setFields(new String[]{"name", "active", "roleOid", "roleOid.name"});
        search.setFilter(new String[][] {{"name", "=", "Kamil"}, {"active", "=", "true"}});
        Query<Map<String, Object>> query = new Query<>(new TransformerImpl<>(session, Criteria.MAP_CLASS));
        Result<Map<String, Object>> result = query.createCriteria(User.class, search).pairList();
        System.out.println(result);
    }
}