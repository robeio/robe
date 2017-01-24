package io.robe.hibernate.criteria.api.criteria.criterion;

import io.robe.hibernate.criteria.api.criterion.Restriction;
import io.robe.hibernate.criteria.api.criterion.RestrictionList;
import io.robe.hibernate.criteria.api.criterion.Restrictions;
import io.robe.hibernate.criteria.query.Operator;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kamilbukum on 18/01/2017.
 */
public class RestrictionsTest {
    @Test
    public void eq() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.EQUALS, "name", "kamil");
        Restriction restriction = Restrictions.eq("name", "kamil");
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void isNull() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.IS_NULL, "name");
        Restriction restriction = Restrictions.isNull("name");
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void ne() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.NOT_EQUALS, "name", "kamil");
        Restriction restriction = Restrictions.ne("name", "kamil");
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void isNotNull() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.IS_NOT_NULL, "name");
        Restriction restriction = Restrictions.isNotNull("name");
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void lt() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.LESS_THAN, "age", 3);
        Restriction restriction = Restrictions.lt("age", 3);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void le() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.LESS_OR_EQUALS_THAN, "age", 3);
        Restriction restriction = Restrictions.le("age", 3);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void gt() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.GREATER_THAN, "age", 3);
        Restriction restriction = Restrictions.gt("age", 3);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void ge() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.GREATER_OR_EQUALS_THAN, "age", 3);
        Restriction restriction = Restrictions.ge("age", 3);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void ilike() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.CONTAINS, "age", "example");
        Restriction restriction = Restrictions.ilike("age", "example");
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void in() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.IN, "age", "3,4");
        Restriction restriction = Restrictions.in("age", "3,4");
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void and() throws Exception {
        Restriction r1 = new Restriction(Operator.EQUALS, "name", "Gol D. Roger");
        Restriction r2 = new Restriction(Operator.IN, "age", "3,4");
        Restriction expectedRestriction = new RestrictionList(Operator.AND, new Restriction[]{r1, r2});
        Restriction restriction = Restrictions.and(r1, r2);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void or() throws Exception {
        Restriction r1 = new Restriction(Operator.EQUALS, "name", "Gol D. Roger");
        Restriction r2 = new Restriction(Operator.IN, "age", "3,4");
        Restriction expectedRestriction = new RestrictionList(Operator.OR, new Restriction[]{r1, r2});
        Restriction restriction = Restrictions.or(r1, r2);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void and1() throws Exception {
        Restriction r1 = new Restriction(Operator.EQUALS, "name", "Gol D. Roger");
        Restriction r2 = new Restriction(Operator.IN, "age", "3,4");
        Restriction expectedRestriction = new RestrictionList(Operator.AND, new Restriction[]{r1, r2});
        List<Restriction> restrictionList = new LinkedList<>();
        restrictionList.add(r1);
        restrictionList.add(r2);
        Restriction restriction = Restrictions.and(restrictionList);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void or1() throws Exception {
        Restriction r1 = new Restriction(Operator.EQUALS, "name", "Gol D. Roger");
        Restriction r2 = new Restriction(Operator.IN, "age", "3,4");
        Restriction expectedRestriction = new RestrictionList(Operator.OR, new Restriction[]{r1, r2});
        List<Restriction> restrictionList = new LinkedList<>();
        restrictionList.add(r1);
        restrictionList.add(r2);
        Restriction restriction = Restrictions.or(restrictionList);
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void filter() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.EQUALS, "name", "kamil");
        Restriction restriction = Restrictions.filter("name", "=", "kamil");
        assertEquals(expectedRestriction, restriction);
    }

    @Test
    public void filter1() throws Exception {
        Restriction expectedRestriction = new Restriction(Operator.EQUALS, "name", "kamil");
        Restriction restriction = Restrictions.filter("name", Operator.EQUALS, "kamil");
        assertEquals(expectedRestriction, restriction);
    }

}