package io.robe.admin.hibernate.dao;


import io.robe.admin.hibernate.entity.Role;
import io.robe.auth.data.entry.RoleEntry;
import io.robe.auth.data.store.RoleStore;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;

import javax.inject.Inject;
import java.util.Optional;

public class RoleDao extends BaseDao<Role> implements RoleStore {

    @Inject
    public RoleDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Optional<? extends RoleEntry> findByRoleId(String oid) {
        return Optional.ofNullable(findById(oid));
    }
}