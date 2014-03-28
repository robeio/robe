package test.com.robe.hibernate.crud; 

import com.robe.hibernate.crud.HibernateCrud;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.util.Properties;

/** 
* HibernateCrud Tester. 
* 
* @author <Authors name> 
* @since <pre>Mar 28, 2014</pre> 
* @version 1.0 
*/ 
public class HibernateCrudTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: generateDaoAndResource(Properties properties) 
* 
*/ 
@Test
public void testGenerateDaoAndResource() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: main(String[] args) 
* 
*/ 
@Test
public void testMain() throws Exception {

    Properties properties = new Properties();
    properties.load(HibernateCrudTest.class.getClassLoader().getResourceAsStream("robe.properties"));
    HibernateCrud.generateDaoAndResource(properties);
} 


} 
