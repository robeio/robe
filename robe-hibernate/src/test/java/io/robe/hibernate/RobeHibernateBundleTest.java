package io.robe.hibernate;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.robe.test.Order;
import io.robe.test.Roadrunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by adem on 19/10/2016.
 */
@RunWith(Roadrunner.class)
public class RobeHibernateBundleTest {

    class TestConfig extends Configuration implements HasHibernateConfiguration {
        @Override
        public HibernateConfiguration getHibernateConfiguration() {
            return new HibernateConfiguration();
        }
    }

    private final String[] packagesToScan = new String[]{"io.robe.hibernate.entity"};
    private final String[] entities = new String[]{"io.robe.hibernate.AnotherTestEntity", "io.robe.hibernate.entity.NotAnnotatedEntity"};

    @Test(expected = RuntimeException.class)
    @Order
    public void getInstanceThrowsRuntimeException() {
        RobeHibernateBundle.getInstance();
    }

    @Test
    @Order(order = 2)
    public void createInstance() {
        RobeHibernateBundle.createInstance(packagesToScan, entities);
    }

    @Test
    @Order(order = 3)
    public void getInstance() {
        RobeHibernateBundle bundle = RobeHibernateBundle.getInstance();
        Assert.assertNotNull(bundle);
    }

    @Test(expected = RuntimeException.class)
    @Order(order = 4)
    public void createInstanceThrowsException() {
        RobeHibernateBundle.createInstance(packagesToScan, entities);
    }

    @Test
    @Order(order = 5)
    public void loadEntities() {
        ImmutableList<Class<?>> loadedEntities = RobeHibernateBundle.loadEntities(packagesToScan, entities);
        Assert.assertEquals(4, loadedEntities.size());
    }

    @Test
    @Order(order = 6)
    public void getHibernate5Module() {
        Hibernate5Module hibernate5Module = RobeHibernateBundle.getInstance().createHibernate5Module();
        Assert.assertNotNull(hibernate5Module);
        Assert.assertFalse(hibernate5Module.isEnabled(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION));
    }

    @Test
    @Order(order = 7)
    public void getDatabaseConfiguration() {
        HibernateConfiguration databaseConfiguration = RobeHibernateBundle.getInstance().getDatabaseConfiguration(new TestConfig());
        Assert.assertNotNull(databaseConfiguration);
    }

    @Test
    @Order(order = 8)
    public void getDatasourceFactory() {
        PooledDataSourceFactory dataSourceFactory = RobeHibernateBundle.getInstance().getDataSourceFactory(new TestConfig());
        Assert.assertNotNull(dataSourceFactory);
    }

    @Test
    @Order(order = 9)
    public void configure() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        RobeHibernateBundle.getInstance().configure(configuration);
        Assert.assertEquals(configuration, RobeHibernateBundle.getInstance().getConfiguration());
    }

}
