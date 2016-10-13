package io.robe.admin.util;

import io.robe.admin.RobeAdminTest;
import io.robe.admin.hibernate.dao.SystemParameterDao;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.hibernate.RobeHibernateBundle;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by hasanmumin on 11/10/2016.
 */
public class SystemParameterCacheTest extends RobeAdminTest {

    @Test
    public void get() {

        SessionFactory sessionFactory = RobeHibernateBundle.getInstance().getSessionFactory();
        ManagedSessionContext.bind(sessionFactory.openSession());
        SystemParameterDao dao = new SystemParameterDao(sessionFactory);

        SystemParameter parameter = new SystemParameter();
        parameter.setValue("ROBE");
        parameter.setKey("TEST");
        parameter = dao.create(parameter);
        dao.flush();

        Object value = SystemParameterCache.get("DEFAULT", "default"); // TODO GuiceBundle not initialized
        Assert.assertTrue(value.equals("default"));

        Object value2 = SystemParameterCache.get("TEST", "none");
        Assert.assertTrue(value2.equals("ROBE"));

        dao.detach(parameter);
        dao.delete(parameter);
        dao.flush();
    }

    @Test
    public void SystemParameterCache() {
        SystemParameterCache cache = new SystemParameterCache();
        Assert.assertTrue("Created new SystemParameterCache instance ", cache != null);
    }
}
