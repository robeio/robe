package io.robe.hibernate.criteria.api.criteria;

import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.Order;
import io.robe.hibernate.criteria.HqlCriteriaTestTools;
import io.robe.hibernate.criteria.api.criterion.Restrictions;
import io.robe.hibernate.criteria.api.projection.Projections;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.Role;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.junit.*;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by kamilbukum on 16/01/2017.
 */
public class CriteriaTest extends HqlCriteriaTestTools {
    @Test
    public void createCriteria() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        assertNotNull(criteria);
    }

    @Test
    public void setOffset() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        criteria.setOffset(2);
        assertEquals(2, (long)criteria.getOffset());
    }

    @Test
    public void setLimit() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        criteria.setLimit(50);
        assertEquals(50, (long)criteria.getLimit());
    }

    @Test
    public void getOffset() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        criteria.setOffset(2);
        assertEquals(2, (long)criteria.getOffset());
    }

    @Test
    public void getLimit() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        criteria.setLimit(50);
        assertEquals(50, (long)criteria.getLimit());
    }

    @Test
    public void list() throws Exception {
        Session session = sessionFactory.openSession();

        // list without any parameter
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        List<User> userList = criteria.list();
        List<User> expectedList = session.createQuery("SELECT user FROM io.robe.hibernate.test.entity.User user").list();
        assertEquals(expectedList, userList);

        // list by limit ( maxresults )
        criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).setLimit(2);
        userList = criteria.list();
        expectedList = session.createQuery("SELECT user FROM io.robe.hibernate.test.entity.User user").setMaxResults(2).list();
        assertEquals(expectedList, userList);

        // list by offset and limit
        criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).setOffset(2).setLimit(2);
        userList = criteria.list();
        expectedList = session.createQuery("SELECT user FROM io.robe.hibernate.test.entity.User user").setFirstResult(2).setMaxResults(2).list();
        assertEquals(expectedList, userList);

    }

    @Test
    public void mapTest(){
        Session session = sessionFactory.openSession();
        // list by offset and limit
        Criteria<Map<String, Object>> mapCriteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<>(session, Criteria.MAP_CLASS))
                .setProjection(Projections.projectionList().add(
                        Projections.property("name"),
                        "name"
                ));
        mapCriteria
                .createJoin("role", Role.class, "roleOid")
                .setProjection(Projections.alias(
                        Projections.property("name"),
                        "role.name"
                ));


        List<Map<String, Object>> mapList = mapCriteria.list();
        System.out.println(mapList);
    }
    @Test
    public void pairList() throws Exception {

    }

    @Test
    public void count() throws Exception {
        Session session = sessionFactory.openSession();

        // simple count
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        long result = criteria.count();
        long expectedResult = (long)session.createQuery("SELECT count(user) FROM io.robe.hibernate.test.entity.User user").uniqueResult();
        assertEquals(result, expectedResult);


        // count with restriction
        criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).add(Restrictions.eq("name",  "Kamil","name"));
        result = criteria.count();
        expectedResult = (long)session.createQuery("" +
                "SELECT count(user) FROM io.robe.hibernate.test.entity.User user " +
                "WHERE user.name = :name"
        ).setParameter("name", "Kamil").uniqueResult();
        assertEquals(expectedResult, result);


        // count with join

        criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).add(Restrictions.eq("name", "Kamil","name"));
        criteria.createJoin("role", Role.class).addRelation("oid", "roleOid");
        result = criteria.count();
        expectedResult = (long)session.createQuery("" +
                "SELECT count(user) FROM io.robe.hibernate.test.entity.User user " +
                "WHERE user.name = :name"
        ).setParameter("name", "Kamil").uniqueResult();
        assertEquals(expectedResult, result);



        criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).add(Restrictions.eq("name",  "Kamil","name"));
        criteria.createJoin("role", Role.class).addRelation("oid", "roleOid").add(Restrictions.eq("name", "Example First Role","name"));
        result = criteria.count();

        expectedResult = (long)session.createQuery("" +
                "SELECT count(*) FROM \n" +
                "io.robe.hibernate.test.entity.User user \n" +
                "LEFT OUTER JOIN io.robe.hibernate.test.entity.Role role  ON \n" +
                "user.roleOid=role.oid AND role.name=:role_name\n" +
                "WHERE user.name=:user_name"
        ).setParameter("user_name", "Kamil").setParameter("role_name", "Example First Role").uniqueResult();
        assertEquals(expectedResult, result);


        criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).add(Restrictions.eq("name", "Kamil","name"));
        criteria.createJoin("role", Role.class).addRelation("oid", "roleOid").add(Restrictions.eq("name", "Example First Role","name"));
        result = criteria.count();
        expectedResult = (long)session.createQuery("" +
                "SELECT count(*) AS DCOUN FROM \n" +
                "io.robe.hibernate.test.entity.User user \n" +
                "LEFT OUTER JOIN io.robe.hibernate.test.entity.Role role  ON \n" +
                "user.roleOid=role.oid AND role.name=:role_name\n" +
                "WHERE user.name=:user_name"
        ).setParameter("user_name", "Kamil").setParameter("role_name", "Example First Role").uniqueResult();
        assertEquals(expectedResult, result);



        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session))
                .add(
                        Restrictions.or(
                                Restrictions.eq("name", "Kamil","name1"),
                                Restrictions.eq("name", "Seray","name2")
                        )
                );
        criteria
                .createJoin("role", Role.class)
                .addRelation("oid", "roleOid")
                .add(Restrictions.eq("name", "Example First Role","name"));

        result = criteria.count();
        expectedResult = (long)session.createQuery("" +
                "SELECT count(*) FROM \n" +
                "io.robe.hibernate.test.entity.User user \n" +
                "LEFT OUTER JOIN io.robe.hibernate.test.entity.Role role  ON \n" +
                "user.roleOid=role.oid AND role.name=:role_name\n" +
                "WHERE user.name=:name1 OR user.name=:name2"
        ).setParameter("name1", "Kamil").setParameter("name2", "Seray").setParameter("role_name", "Example First Role").uniqueResult();
        assertEquals(expectedResult, result);



        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session))
                .add(
                        Restrictions.and(
                                Restrictions.or(
                                        Restrictions.eq("name", "Kamil","name1"),
                                        Restrictions.eq("name", "Seray","name2")
                                ),
                                Restrictions.eq("active", false, "activeAlias")
                        )
                );
        criteria
                .createJoin("role", Role.class)
                .addRelation("oid", "roleOid")
                .add(Restrictions.eq("name", "Example First Role","name"));

        result = criteria.count();
        expectedResult = (long)session.createQuery("" +
                "SELECT count(*) FROM \n" +
                "io.robe.hibernate.test.entity.User user \n" +
                "LEFT OUTER JOIN io.robe.hibernate.test.entity.Role role  ON \n" +
                "user.roleOid=role.oid AND role.name=:role_name\n" +
                "WHERE (user.name=:name1 OR user.name=:name2) AND user.active=:activeAlias"
        ).setParameter("name1", "Kamil").setParameter("name2", "Seray").setParameter("activeAlias", false).setParameter("role_name", "Example First Role").uniqueResult();
        assertEquals(expectedResult, result);
    }

    @Test
    public void uniqueResult() throws Exception {
        Session session = sessionFactory.openSession();

        // simple count
        Criteria<User> criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session))
                .add(
                        Restrictions.and(
                                Restrictions.or(
                                        Restrictions.eq("name", "Kamil","name1"),
                                        Restrictions.eq("name", "Seray","name2")
                                ),
                                Restrictions.eq("active", false, "activeAlias")
                        )
                );
        criteria
                .createJoin("role", Role.class)
                .addRelation("oid", "roleOid")
                .add(Restrictions.eq("name", "Example First Role","name"));

        User user = (User) criteria.uniqueResult();
        User expectedResult = (User) session.createQuery("" +
                "SELECT user FROM \n" +
                "io.robe.hibernate.test.entity.User user \n" +
                "LEFT OUTER JOIN io.robe.hibernate.test.entity.Role role  ON \n" +
                "user.roleOid=role.oid AND role.name=:role_name\n" +
                "WHERE (user.name=:name1 OR user.name=:name2) AND user.active=:activeAlias"
        ).setParameter("name1", "Kamil").setParameter("name2", "Seray").setParameter("activeAlias", false).setParameter("role_name", "Example First Role").uniqueResult();
        assertEquals(expectedResult, expectedResult);
    }

    @Test
    public void getTransformer() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session));
        assertNotNull(criteria.getTransformer());
    }

    @Test
    public void getOrders() throws Exception {
        Session session = sessionFactory.openSession();

        List<User> expectedResult = session.createQuery("SELECT user\n" +
                        "FROM io.robe.hibernate.test.entity.User user  ORDER BY user.name ASC,user.active DESC").list();

        Criteria<User> criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).addOrder(Order.asc("name")).addOrder(Order.desc("active"));
        List<User> users = criteria.list();
        assertEquals(expectedResult, users);


        expectedResult = session.createQuery("SELECT user\n" +
                "FROM io.robe.hibernate.test.entity.User user  ORDER BY user.name ASC,user.active DESC").list();

        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session))
                .addOrder(Order.asc("name"))
                .addOrder(Order.desc("active"));
        criteria.createJoin("role", Role.class, "roleOid").addOrder(Order.asc("name"));
        users = criteria.list();
        System.out.println(expectedResult);
        assertEquals(expectedResult, users);
    }
}