package io.robe.admin.util;

import io.robe.admin.hibernate.dao.SystemParameterDao;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.hibernate.RobeHibernateBundle;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SystemParameterCache {

    private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();


    // TODO this method should be refactoring.
    public static void fillCache() {

        SessionFactory sessionFactory = RobeHibernateBundle.getInstance().getSessionFactory();
        ManagedSessionContext.bind(sessionFactory.openSession());
        SystemParameterDao dao = new SystemParameterDao(sessionFactory);

        List<SystemParameter> parameters = dao.findAll();

        for (SystemParameter parameter : parameters) {
            cache.put(parameter.getKey(), parameter.getValue());
            dao.detach(parameter);// TODO
        }

    }


    public static Object get(String key, Object defaultValue) {
        if (cache.isEmpty()) {
            fillCache();
        }
        Object value = cache.get(key);
        return (value == null) ? defaultValue : value;
    }

}
