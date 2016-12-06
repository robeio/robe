package io.robe.hibernate.criteria.impl.hql;

import io.robe.common.service.search.model.SearchModel;
import io.robe.hibernate.HibernateUtil;
import io.robe.hibernate.criteria.api.ResultPair;
import io.robe.hibernate.test.entity.Role;
import io.robe.hibernate.test.entity.User;
import io.robe.hibernate.test.entity.UserDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * Created by kamilbukum on 02/12/16.
 */
public class SearchQueryHQLTest {

    private static SessionFactory sessionFactory = null;

    protected static String IO_ROBE_ADMIN = "io/robe/admin";
    protected static String ADMIN = "Admin";
    protected static String EMAIL = "admin@robe.io";
    private static Role role;
    private static User user;
    private static String[][] filter = new String[][]{
            {
                    "email",
                    "=",
                    "admin@robe.io"
            },
            {
                    "roleOid.name",
                    "|=",
                    "Role Name"
            }
    };
    private static String[] sortings = {
         "+roleOid.name",
         "-email"
    };


    static  {
        role = new Role();
        role.setCode("Role Code");
        role.setName("Role Name");

        user = new User();
        user.setEmail(EMAIL);
        user.setActive(true);
        user.setName(IO_ROBE_ADMIN);
        user.setSurname(IO_ROBE_ADMIN);
        user.setPassword("123123");
        user.setRoleOid(role.getOid());

    }


    @BeforeClass
    public static void create(){
        sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.persist(role);
        user.setRoleOid(role.getOid());
        session.persist(user);
        session.getTransaction().commit();
    }



    @Test
    public void pairListStrict() throws Exception {


        // Create Search Model
        SearchModel searchModel = new SearchModel();
        searchModel.setFilter(filter);
        searchModel.setSort(sortings);
        Session session = sessionFactory.openSession();


        ResultPair<List<User>, Long> resultPair = SearchQueryHQL.pairListStrict(session, User.class, searchModel);

        System.out.println(resultPair.getLeft());
        System.out.println(resultPair.getRight());
    }

    @Test
    public void pairList() throws Exception {


        // Create Search Model
        SearchModel searchModel = new SearchModel();
        searchModel.setFilter(filter);
        searchModel.setQ("Example");
        // searchModel.setFields(new String[] {"email"});
        Session session = sessionFactory.openSession();

        ResultPair<List<Map<String, Object>>, Long> resultPair = SearchQueryHQL.pairList(session, User.class, searchModel);

        System.out.println(resultPair.getLeft());
        System.out.println(resultPair.getRight());
    }

    @Test
    public void pairListBean() throws Exception {


        // Create Search Model
        SearchModel searchModel = new SearchModel();

        searchModel.setFilter(filter);
        searchModel.setFields(new String[] {"email"});
        searchModel.setSort(new String[] {"email","roleOid.oid"});
        Session session = sessionFactory.openSession();

        ResultPair<List<UserDTO>, Long> resultPair = SearchQueryHQL.pairList(session, User.class, searchModel, UserDTO.class);

        System.out.println(resultPair.getLeft());
        System.out.println(resultPair.getRight());
    }

    @Test
    public void listStrict() throws Exception {


        // Create Search Model
        SearchModel searchModel = new SearchModel();
        searchModel.setFilter(filter);


        Session session = sessionFactory.openSession();

        List<User> list = SearchQueryHQL.listStrict(session, User.class, searchModel);

        System.out.println(list);
    }

    @Test
    public void list() throws Exception {

        // Create Search Model
        SearchModel searchModel = new SearchModel();
        searchModel.setFilter(filter);

        Session session = sessionFactory.openSession();

        List<Map<String, Object>> list = SearchQueryHQL.list(session, User.class, searchModel);

        System.out.println(list);
    }

    @Test
    public void list1() throws Exception {

        // Create Search Model
        SearchModel searchModel = new SearchModel();
        searchModel.setFilter(filter);


        Session session = sessionFactory.openSession();

        List<User> list = SearchQueryHQL.list(session, User.class, searchModel, User.class);

        System.out.println(list);
    }

}