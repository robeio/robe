package io.robe.hibernate.query.impl.hql.util;

import io.robe.hibernate.query.HqlCriteriaTestTools;
import io.robe.hibernate.query.api.criteria.Criteria;
import io.robe.hibernate.query.api.criteria.projection.Projections;
import io.robe.hibernate.query.impl.hql.TransformerImpl;
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

        TransformerImpl<?> transformer = new TransformerImpl<>(session);
        // property test
        criteria.setProjection(Projections.property("name"));
        String expectedResult = "user.name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // rowCount Test
        criteria.setProjection(Projections.rowCount());
        expectedResult = "COUNT(1)";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // AVG
        criteria.setProjection(Projections.avg("age"));
        expectedResult = "AVG(user.age)";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // ProjectionList
        criteria.setProjection(Projections.projectionList().add(Projections.rowCount()).add(Projections.property("name")));
        expectedResult = "COUNT(1), user.name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // alias
        criteria.setProjection(Projections.alias(Projections.property("name"), "name"));
        expectedResult = "user.name AS name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // alias
        criteria.setProjection(Projections.groupProperty("name"));
        expectedResult = "user.name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // alias
        criteria.setProjection(Projections.count("name"));
        expectedResult = "COUNT(user.name)";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // id projection
        criteria.setProjection(Projections.id());
        expectedResult = "user.oid";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // max projection
        criteria.setProjection(Projections.max("age"));
        expectedResult = "MAX(user.age)";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // min projection
        criteria.setProjection(Projections.min("age"));
        expectedResult = "MIN(user.age)";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // sum projection
        criteria.setProjection(Projections.sum("age"));
        expectedResult = "SUM(user.age)";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

        // Complex Projection
        criteria.setProjection(
                Projections
                        .projectionList()
                        .add(Projections.rowCount())
                        .add(Projections.property("name"))
                        .add(Projections.max("age"))
                        .add(Projections.groupProperty("name")));
        expectedResult = "COUNT(1), user.name, MAX(user.age), user.name";
        assertEquals(expectedResult, SelectUtil.generateSelectQueryForList(criteria, transformer));

    }
}