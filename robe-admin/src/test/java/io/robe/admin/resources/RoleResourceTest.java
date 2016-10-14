package io.robe.admin.resources;

import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.entity.Role;
import io.robe.admin.util.request.TestRequest;
import io.robe.admin.util.request.TestResponse;
import io.robe.hibernate.RobeHibernateBundle;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/**
 * Created by hasanmumin on 03/10/16.
 */
public class RoleResourceTest extends BaseResourceTest<Role> {

    @Override
    public String getPath() {
        return "roles";
    }

    @Override
    public Class<Role> getClazz() {
        return Role.class;
    }

    @Override
    public void assertEquals(Role model, Role response) {
        Assert.assertEquals(model.getName(), response.getName());
        Assert.assertEquals(model.getCode(), response.getCode());
    }

    @Override
    public void assertEquals(Role mergeInstance, Role original, Role response) {
        Assert.assertEquals(mergeInstance.getName(), response.getName());
        Assert.assertEquals(original.getCode(), response.getCode());
    }

    @Override
    public Role instance() {

        Role role = new Role();
        role.setCode("CODE");
        role.setName("Name");
        return role;
    }

    @Override
    public Role update(Role response) {
        response.setName("Name-1");
        return response;
    }

    @Override
    public Role mergeInstance() {

        Role role = new Role();
        role.setName("Name-2");
        return role;
    }

    @Test
    public void getRolePermissions() throws IOException {
        SessionFactory sessionFactory = RobeHibernateBundle.getInstance().getSessionFactory();
        ManagedSessionContext.bind(sessionFactory.openSession());
        RoleDao roleDao = new RoleDao(sessionFactory);
        Role role = roleDao.findByCode("all");
        Assert.assertTrue(role != null);
        TestRequest request = requestBuilder.endpoint(role.getId() + "/permissions").build();
        TestResponse response = client.get(request);
        Map result = response.get(Map.class);

        Assert.assertTrue(result.get("menu") != null);
        Assert.assertTrue(result.get("service") != null);
        ManagedSessionContext.unbind(sessionFactory);
    }
}
