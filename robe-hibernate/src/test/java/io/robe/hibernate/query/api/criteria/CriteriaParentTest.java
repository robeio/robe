package io.robe.hibernate.query.api.criteria;

import io.robe.common.utils.TypeReference;
import io.robe.hibernate.query.HqlCriteriaTestTools;
import io.robe.hibernate.query.api.criteria.criterion.Restrictions;
import io.robe.hibernate.query.api.criteria.projection.Projections;
import io.robe.hibernate.query.impl.hql.TransformerImpl;
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
                .createCriteria("user", User.class, new TransformerImpl<>(session, Map.class)).setProjection(
                        Projections.projectionList().add(Projections.property("name")).add(Projections.property("failCount"))
                ).add(Restrictions.eq("name", "Kamil", "name"));
        Object resultProperty = criteria.uniqueResult();
        System.out.println(resultProperty);

        // rowCount
        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.rowCount());
        long result = (long)criteria.uniqueResult();
        assertEquals(result, expectedUserList.size());
        // count
        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.count("name"));
        result = (long)criteria.uniqueResult();
        assertEquals(result, expectedUserList.size());

        // alias
        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.alias(Projections.count("name"), "name"));
        result = (long)criteria.uniqueResult();
        assertEquals(result, expectedUserList.size());

        // sum
        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.sum("failCount"));
        result = (long) criteria.uniqueResult();
        long expected = expectedUserList.stream().mapToInt((user) -> user.getFailCount()).sum();
        assertEquals(result, expected);


        // max
        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.max("failCount"));
         result = (int) criteria.uniqueResult();
         expected = expectedUserList.stream().mapToInt((user) -> user.getFailCount()).max().getAsInt();
        assertEquals(result, expected);


        // min
        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.min("failCount"));
        result = (int) criteria.uniqueResult();
        expected = expectedUserList.stream().mapToInt((user) -> user.getFailCount()).min().getAsInt();
        assertEquals(result, expected);


        // avg
        criteria = Criteria
                .createCriteria("user", User.class, new TransformerImpl<User>(session)).setProjection(Projections.avg("failCount"));
        double resultAvg = (double) criteria.uniqueResult();
        double expectedAvg = expectedUserList.stream().mapToInt((user) -> user.getFailCount()).average().getAsDouble();
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