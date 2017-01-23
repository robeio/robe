package io.robe.hibernate.criteria.impl.hql.util;

import io.robe.hibernate.criteria.hql.util.RestrictionUtil;
import io.robe.hibernate.criteria.HqlCriteriaTestTools;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.api.criterion.Restriction;
import io.robe.hibernate.criteria.api.criterion.Restrictions;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

import static org.junit.Assert.*;

/**
 * Created by kamilbukum on 17/01/2017.
 */
public class RestrictionUtilTest extends HqlCriteriaTestTools {

    @Test
    public void generateRestrictionsQuery() throws Exception {

    }

    @Test
    public void generateRestrictions() throws Exception {

        Session session = sessionFactory.openSession();
        Criteria<User> criteria = Criteria.createCriteria("user", User.class, new TransformerImpl<User>(session));

        // IS NULL
        criteria.add(Restrictions.isNull("name"));
        String expectedResult = "user.name IS NULL";
        StringJoiner andJoiner = new StringJoiner(" AND ");
        StringJoiner qJoiner = new StringJoiner(" OR ");
        Map<String, Object> parameterMap = new LinkedHashMap<>();
        RestrictionUtil.generateRestrictions(criteria, criteria.getRestrictions(), andJoiner, qJoiner, parameterMap);
        assertEquals(expectedResult, andJoiner.toString());

        // IS NULL AND EQUALS
        criteria.add(Restrictions.eq("name", "Kamil","userName"));
        expectedResult = "user.name IS NULL AND user.name=:$userName";
        andJoiner = new StringJoiner(" AND ");
        qJoiner = new StringJoiner(" OR ");
        parameterMap.clear();
        RestrictionUtil.generateRestrictions(criteria, criteria.getRestrictions(), andJoiner, qJoiner, parameterMap);
        assertEquals(expectedResult, andJoiner.toString());

        // IS NULL AND EQUALS AND GREATER THEN
        criteria.add(Restrictions.gt("age", "Kamil","age"));
        expectedResult = "user.name IS NULL AND user.name=:$userName AND user.age > :$age";
        andJoiner = new StringJoiner(" AND ");
        qJoiner = new StringJoiner(" OR ");
        parameterMap.clear();
        RestrictionUtil.generateRestrictions(criteria, criteria.getRestrictions(), andJoiner, qJoiner, parameterMap);
        assertEquals(expectedResult, andJoiner.toString());


        // IS NULL AND EQUALS AND GREATER THEN OR (user.name EQUALS userName);
        criteria.add(Restrictions.or(new Restriction[]{Restrictions.ilike("name", "Kamil","name2"), Restrictions.gt("age", "Kamil","age2")}));
        expectedResult = "user.name IS NULL AND user.name=:$userName AND user.age > :$age AND ( user.name LIKE :$name2 OR user.age > :$age2 )";
        andJoiner = new StringJoiner(" AND ");
        qJoiner = new StringJoiner(" OR ");
        RestrictionUtil.generateRestrictions(criteria, criteria.getRestrictions(), andJoiner, qJoiner, parameterMap);
        assertEquals(expectedResult, andJoiner.toString());
    }

}