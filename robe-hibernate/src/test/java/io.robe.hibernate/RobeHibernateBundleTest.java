package io.robe.hibernate;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.db.PooledDataSourceFactory;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Created by adem on 19/10/2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RobeHibernateBundleTest {

    class TestConfig extends Configuration implements HasHibernateConfiguration {
        @Override
        public HibernateConfiguration getHibernateConfiguration() {
            return new HibernateConfiguration();
        }
    }

    private final String[] packagesToScan = new String[]{"io.robe.hibernate.entity"};
    private final String[] entities = new String[]{"io.robe.hibernate.AnotherTestEntity", "io.robe.hibernate.entity.NotAnnotatedEntity", "io.robe.hibernate.NoEntity"};

    @Test(expected = RuntimeException.class)
    public void test0_getInstanceThrowsRuntimeException() {
        RobeHibernateBundle.getInstance();
    }

    @Test
    public void test1_createInstance() {
        RobeHibernateBundle.createInstance(packagesToScan, entities);
    }

    @Test
    public void test2_getInstance() {
        RobeHibernateBundle bundle = RobeHibernateBundle.getInstance();
        Assert.assertNotNull(bundle);
    }

    @Test(expected = RuntimeException.class)
    public void test3_createInstanceThrowsException() {
        RobeHibernateBundle.createInstance(packagesToScan, entities);
    }

    @Test
    public void test4_loadEntities() {
        ImmutableList<Class<?>> loadedEntities = RobeHibernateBundle.loadEntities(packagesToScan, entities);
        Assert.assertEquals(4, loadedEntities.size());
    }

    @Test
    public void test5_getHibernate5Module() {
        Hibernate5Module hibernate5Module = RobeHibernateBundle.getInstance().createHibernate5Module();
        Assert.assertNotNull(hibernate5Module);
        Assert.assertFalse(hibernate5Module.isEnabled(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION));
    }

    @Test
    public void test6_getDatabaseConfiguration() {
        HibernateConfiguration databaseConfiguration = RobeHibernateBundle.getInstance().getDatabaseConfiguration(new TestConfig());
        Assert.assertNotNull(databaseConfiguration);
    }

    @Test
    public void test7_getDatasourceFactory() {
        PooledDataSourceFactory dataSourceFactory = RobeHibernateBundle.getInstance().getDataSourceFactory(new TestConfig());
        Assert.assertNotNull(dataSourceFactory);
    }

    @Test
    public void test8_configure() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        RobeHibernateBundle.getInstance().configure(configuration);
        Assert.assertEquals(configuration, RobeHibernateBundle.getInstance().getConfiguration());
    }

}
