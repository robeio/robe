package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.Role;

/**
 * Created by hasanmumin on 07/10/2016.
 */
public class RoleDaoTest extends BaseDaoTest<Role, RoleDao> {
    @Override
    public Role instance() {
        Role role = new Role();
        role.setName("ROLE_NAME");
        role.setCode("ROLE_CODE");
        return role;
    }

    @Override
    public Role update(Role model) {
        model.setCode("ROLE_CODE_1");
        model.setName("ROLE_NAME_1");
        return model;
    }
}
