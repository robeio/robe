package io.robe.hibernate.criteria.api.criteria;

import io.robe.common.utils.TypeReference;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.CriteriaJoin;
import io.robe.hibernate.criteria.HqlCriteriaTestTools;
import io.robe.hibernate.criteria.api.criterion.Restrictions;
import io.robe.hibernate.criteria.api.projection.Projections;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.Role;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by kamilbukum on 18/01/2017.
 */
public class CriteriaParentTest extends HqlCriteriaTestTools {
    private static final double DELTA = 1e-15;

    private static final TypeReference<Map<String, Object>>  MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {};
    @Test
    public void createJoin() throws Exception {

    }

    @Test
    public void createJoin1() throws Exception {

    }

    @Test
    public void getJoin() throws Exception {

    }

    @Test
    public void getJoins() throws Exception {

    }

    @Test
    public void getProjection() throws Exception {

    }

    @Test
    public void add() throws Exception {

    }

    @Test
    public void getRestrictions() throws Exception {

    }

    @Test
    public void getAlias() throws Exception {

    }

    @Test
    public void addOrder() throws Exception {

    }

    @Test
    public void getEntityClass() throws Exception {

    }

    @Test
    public void getIdentityName() throws Exception {

    }

    @Test
    public void setProjection() throws Exception {
        Session session = sessionFactory.openSession();
        List<User> expectedUserList = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).list();

        // rowCount
        Criteria<Map<String, Object>> criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<>(session, MAP_TYPE_REFERENCE.getClazz()))
                .setProjection(
                        Projections.projectionList().add(Projections.property("name")).add(Projections.property("failCount"))
                ).add(Restrictions.eq("name", "Kamil", "name"));
        Object resultProperty = criteria.uniqueResult();
        System.out.println(resultProperty);

        // rowCount
        Criteria<User> userCriteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.rowCount());
        long result = (long)userCriteria.uniqueResult();
        assertEquals(result, expectedUserList.size());
        // count
        userCriteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.count("name"));
        result = (long)userCriteria.uniqueResult();
        assertEquals(result, expectedUserList.size());

        // alias
        userCriteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.alias(Projections.count("name"), "name"));
        result = (long)userCriteria.uniqueResult();
        assertEquals(result, expectedUserList.size());

        // sum
        userCriteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.sum("failCount"));
        result = (long) userCriteria.uniqueResult();
        long expected = expectedUserList.stream().mapToInt(User::getFailCount).sum();
        assertEquals(result, expected);


        // max
        userCriteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.max("failCount"));
         result = (int) userCriteria.uniqueResult();
         expected = expectedUserList.stream().mapToInt(User::getFailCount).max().getAsInt();
        assertEquals(result, expected);


        // min
        userCriteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.min("failCount"));
        result = (int) userCriteria.uniqueResult();
        expected = expectedUserList.stream().mapToInt(User::getFailCount).min().getAsInt();
        assertEquals(result, expected);


        // avg
        userCriteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.avg("failCount"));
        double resultAvg = (double) userCriteria.uniqueResult();
        double expectedAvg = expectedUserList.stream().mapToInt(User::getFailCount).average().getAsDouble();
        assertEquals(resultAvg, expectedAvg, DELTA);
    }

    @Test
    public void getTransformer() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session));
        CriteriaJoin join = criteria.createJoin("role", Role.class, "roleOid");
        assertNotNull(criteria.getTransformer());
        assertNotNull(join.getTransformer());
    }

}