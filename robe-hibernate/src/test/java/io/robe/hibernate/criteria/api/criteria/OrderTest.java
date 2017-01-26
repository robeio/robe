package io.robe.hibernate.criteria.api.criteria;

import io.robe.hibernate.criteria.api.Order;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kamilbukum on 17/01/2017.
 */
public class OrderTest {

    @Test
    public void asc() throws Exception {
        String name = "name";
        Order order = Order.asc(name);
        assertEquals(order.getType(), Order.Type.ASC);
        assertEquals(order.getName(), name);
    }

    @Test
    public void desc() throws Exception {
        String name = "name";
        Order order = Order.desc(name);
        assertEquals(order.getType(), Order.Type.DESC);
        assertEquals(order.getName(), name);
    }

    @Test
    public void getAlias() throws Exception {
        String name = "name";
        String alias = "nameAlias";
        Order order = Order.asc(name);
        assertEquals(order.getType(), Order.Type.ASC);
        assertEquals(order.getName(), name);
    }

    @Test
    public void setAlias() throws Exception {
        String name = "name";
        Order order = Order.asc(name);
        assertEquals(order.getType(), Order.Type.ASC);
        assertEquals(order.getName(), name);
        assertNull(order.getCriteriaAlias());
    }

    @Test
    public void getName() throws Exception {
        String name = "name";
        Order order = Order.asc(name);
        assertEquals(order.getName(), name);
    }

    @Test
    public void getType() throws Exception {
        String name = "name";
        Order order = Order.asc(name);
        assertEquals(order.getType(), Order.Type.ASC);
    }

}