package io.robe.admin.resources;

import io.robe.admin.hibernate.entity.Role;
import org.junit.Assert;

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
        Assert.assertEquals(model, response);
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
}
