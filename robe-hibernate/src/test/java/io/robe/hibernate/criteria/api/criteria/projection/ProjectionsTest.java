package io.robe.hibernate.criteria.api.criteria.projection;

import io.robe.hibernate.criteria.api.projection.*;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by kamilbukum on 18/01/2017.
 */
public class ProjectionsTest {
    @Test
    public void property() throws Exception {
        PropertyProjection expectedProjection = new PropertyProjection("name", false);
        Projection projection = Projections.property("name");
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void groupProperty() throws Exception {
        PropertyProjection expectedProjection = new PropertyProjection("name", true);
        Projection projection = Projections.groupProperty("name");
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void id() throws Exception {
        IdentifierProjection expectedProjection = new IdentifierProjection();
        Projection projection = Projections.id();
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void projectionList() throws Exception {
        ProjectionList expectedProjection = Projections.projectionList();
        PropertyProjection p1 = new PropertyProjection("name", true);
        PropertyProjection p2 = new PropertyProjection("name", false);
        expectedProjection.add(p1).add(p2);
        Projection projection = Projections.projectionList().add(p1).add(p2);
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void rowCount() throws Exception {
        FunctionProjection expectedProjection = new FunctionProjection(null, FunctionProjection.Type.COUNT);
        Projection projection = Projections.rowCount();
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void count() throws Exception {
        FunctionProjection expectedProjection = new FunctionProjection("name", FunctionProjection.Type.COUNT);
        Projection projection = Projections.count("name");
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void max() throws Exception {
        FunctionProjection expectedProjection = new FunctionProjection("age", FunctionProjection.Type.MAX);
        Projection projection = Projections.max("age");
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void min() throws Exception {
        FunctionProjection expectedProjection = new FunctionProjection("age", FunctionProjection.Type.MIN);
        Projection projection = Projections.min("age");
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void avg() throws Exception {
        FunctionProjection expectedProjection = new FunctionProjection("age", FunctionProjection.Type.AVG);
        Projection projection = Projections.avg("age");
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void sum() throws Exception {
        FunctionProjection expectedProjection = new FunctionProjection("age", FunctionProjection.Type.SUM);
        Projection projection = Projections.sum("age");
        assertEquals(expectedProjection, projection);
    }

    @Test
    public void alias() throws Exception {
        Projection p = new PropertyProjection("name", false);
        EnhancedProjection expectedProjection = new EnhancedProjection(p, "nameProjectionAlias");
        Projection projection = Projections.alias(p, "nameProjectionAlias");
        assertEquals(expectedProjection, projection);
    }

}