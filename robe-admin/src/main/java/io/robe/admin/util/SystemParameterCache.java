package io.robe.admin.util;

import io.robe.admin.hibernate.dao.SystemParameterDao;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.guice.GuiceBundle;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SystemParameterCache {

    private static ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();


    public static void fillCache() {
        SystemParameterDao dao = GuiceBundle.getInjector().getInstance(SystemParameterDao.class);

        List<SystemParameter> parameters = dao.findAll(SystemParameter.class);

        for (SystemParameter parameter : parameters)
            cache.put(parameter.getKey(), parameter.getValue());
    }


    public static Object get(String key, Object defaultValue) {
        Object value = cache.get(key);
        return (value == null) ? defaultValue : value;
    }

}
