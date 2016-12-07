package io.robe.hibernate.criteria.impl.hql;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.HibernateUtil;
import io.robe.common.dto.Pair;
import io.robe.hibernate.test.entity.Role;
import io.robe.hibernate.test.entity.User;
import io.robe.hibernate.test.entity.UserDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

/**
 * Created by kamilbukum on 02/12/16.
 */
public class SearchQueryHQLTest {

    private static SessionFactory sessionFactory = null;
    private static Random rand = new Random();
    private static List<Role> roles = new LinkedList<>();
    private static List<User> users = new LinkedList<>();
    private static String[] fields;
    private static String[][] filters;
    private static String[] sortings;
    private static Integer limit;
    private static Integer offset;
    static  {

        fields = new String[] {"email", "roleOid.name"};

        filters = new String[][]{
                {
                        "roleOid.name",
                        "|=",
                        "Example First Role|Example Second Role|Example Fifth Role"
                }
        };

        sortings = new String[] {
                "+roleOid.name",
                "-email"
        };

        limit = 2;

        offset = 1;

        addRolesAndUsers();
    }

    public SearchModel getModel(boolean isField, boolean isFilter, boolean isSorting, boolean isPaging){
        SearchModel model = new SearchModel();
        if(isField) {
            model.setFields(fields);
        }
        if(isFilter) {
            model.setFilter(filters);
        }
        if(isSorting) {
            model.setSort(sortings);
        }

        if(isPaging) {
            model.setLimit(limit);
            model.setOffset(offset);
        }

        return model;
    }


    @BeforeClass
    public static void create(){
        sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();

        if(roles.size() > 0) {
            for(Role role: roles) {
                session.persist(role);
            }
        }

        if(users.size() > 0 && roles.size() > 0) {
            for(User user: users) {
                Role role = roles.get(0);
                user.setRoleOid(role.getOid());
                session.persist(user);
            }
        }

        session.getTransaction().commit();
    }



    @Test
    public void pairListStrict() throws Exception {
        Session session = sessionFactory.openSession();

        Pair<List<User>, Long> resultPair = SearchQueryHQL.pairListStrict(session, User.class, getModel(
                true,
                true,
                true,
                true
        ));

        System.out.println(resultPair.getLeft());
        System.out.println(resultPair.getRight());
    }

    @Test
    public void pairList() throws Exception {


        // searchModel.setFields(new String[] {"email"});
        Session session = sessionFactory.openSession();

        Pair<List<Map<String, Object>>, Long> resultPair = SearchQueryHQL.pairList(session, User.class, getModel(
                true,
                true,
                true,
                true
        ));

        System.out.println(resultPair.getLeft());
        System.out.println(resultPair.getRight());
    }

    @Test
    public void pairListBean() throws Exception {

        Session session = sessionFactory.openSession();

        Pair<List<UserDTO>, Long> resultPair = SearchQueryHQL.pairList(session, User.class, getModel(
                true,
                true,
                true,
                true
        ), UserDTO.class);

        System.out.println(resultPair.getLeft());
        System.out.println(resultPair.getRight());
    }

    @Test
    public void listStrict() throws Exception {

        Session session = sessionFactory.openSession();

        List<User> list = SearchQueryHQL.listStrict(session, User.class, getModel(
                true,
                true,
                true,
                true
        ));

        System.out.println(list);
    }

    @Test
    public void list() throws Exception {

        Session session = sessionFactory.openSession();

        List<Map<String, Object>> list = SearchQueryHQL.list(session, User.class, getModel(
                true,
                true,
                true,
                true
        ));

        System.out.println(list);
    }

    @Test
    public void list1() throws Exception {

        Session session = sessionFactory.openSession();

        List<User> list = SearchQueryHQL.list(session, User.class, getModel(
                true,
                true,
                true,
                true
        ), User.class);

        System.out.println(list);
    }


    private static void addRolesAndUsers() {

        roles.add(new Role("Role1", "Example First Role"));
        roles.add(new Role("Role2", "Example Second Role"));
        roles.add(new Role("Role3", "Example Third Role"));
        roles.add(new Role("Role4", "Example Fourth Role"));
        roles.add(new Role("Role5", "Example Fifth Role"));

        users.add(new User(
                "admin@robe.io",
                "Kamil",
                "Bukum",
                "35345435345345",
                true,
                0,
                new Date(),
                new Date(),
                null,
                "Transient Value"
        ));

        users.add(new User(
                "seray@robe.io",
                "Seray",
                "Uzgur",
                "35345435345345",
                true,
                0,
                new Date(),
                new Date(),
                null,
                "Transient Value"
        ));

        users.add(new User(
                "hasan@robe.io",
                "Hasan",
                "Mumin",
                "35345435345345",
                true,
                0,
                new Date(),
                new Date(),
                null,
                "Transient Value"
        ));
    }



}