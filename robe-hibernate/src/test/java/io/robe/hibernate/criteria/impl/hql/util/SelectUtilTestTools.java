package io.robe.hibernate.criteria.impl.hql.util;

import io.robe.hibernate.criteria.hql.util.SelectUtil;
import io.robe.hibernate.criteria.HqlCriteriaTestTools;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.projection.Projections;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kamilbukum on 17/01/2017.
 */
public class SelectUtilTestTools extends HqlCriteriaTestTools {
    @Test
    public void generateSelectQueryForList() throws Exception {
        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));
        // property test
        criteria.setProjection(Projections.property("name"));
        String expectedResult = "user.name AS user_0_name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // rowCount Test
        criteria.setProjection(Projections.rowCount());
        expectedResult = "COUNT(1) AS user_0_count";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // AVG
        criteria.setProjection(Projections.avg("age"));
        expectedResult = "AVG(user.age) AS user_0_avg";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // ProjectionList
        criteria.setProjection(Projections.projectionList().add(Projections.rowCount()).add(Projections.property("name")));
        expectedResult = "COUNT(1) AS user_0_count, user.name AS user_0_name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // alias
        criteria.setProjection(Projections.alias(Projections.property("name"), "name"));
        expectedResult = "user.name AS name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // alias
        criteria.setProjection(Projections.groupProperty("name"));
        expectedResult = "user.name AS user_0_name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // alias
        criteria.setProjection(Projections.count("name"));
        expectedResult = "COUNT(user.name) AS user_0_count";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // id projection
        criteria.setProjection(Projections.id());
        expectedResult = "user.oid AS user_0_oid";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // max projection
        criteria.setProjection(Projections.max("age"));
        expectedResult = "MAX(user.age) AS user_0_max";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // min projection
        criteria.setProjection(Projections.min("age"));
        expectedResult = "MIN(user.age) AS user_0_min";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // sum projection
        criteria.setProjection(Projections.sum("age"));
        expectedResult = "SUM(user.age) AS user_0_sum";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

        // Complex Projection
        criteria.setProjection(
                Projections
                        .projectionList()
                        .add(Projections.rowCount())
                        .add(Projections.property("name"))
                        .add(Projections.max("age"))
                        .add(Projections.groupProperty("name")));
        expectedResult = "COUNT(1) AS user_0_count, user.name AS user_0_name, MAX(user.age) AS user_0_max, user.name AS user_0_name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria));

    }
}