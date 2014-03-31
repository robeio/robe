package io.robe.hibernate.crud;

import org.junit.Test;

import java.util.Properties;

public class HibernateCrudTest {

    @Test
    public void testMain() throws Exception {

        Properties properties = new Properties();
        properties.load(HibernateCrudTest.class.getClassLoader().getResourceAsStream("robe.properties"));
        HibernateCrud.generateDaoAndResource(properties);
    }


} 
