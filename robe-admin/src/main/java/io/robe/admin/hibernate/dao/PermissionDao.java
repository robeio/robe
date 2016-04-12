package io.robe.admin.hibernate.dao;

import javax.inject.Inject;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.auth.data.entry.PermissionEntry;
import io.robe.auth.data.store.PermissionStore;
import io.robe.hibernate.dao.BaseDao;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionDao extends BaseDao<Permission> implements PermissionStore {

    @Inject
    public PermissionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Permission findByRoleAndItem(String roleOid, BaseEntity restrictedItem) {
        Criteria criteria = currentSession().createCriteria(Permission.class);
        criteria.add(Restrictions.eq("roleOid", roleOid));
        criteria.add(Restrictions.eq("restrictedItemOid", restrictedItem.getOid()));
        return uniqueResult(criteria);
    }

    public List<Permission> findByRoleOId(String roleOid) {
        Criteria criteria = currentSession().createCriteria(Permission.class);
        criteria.add(Restrictions.eq("roleOid", roleOid));
        return list(criteria);
    }

    public void deleteRestrictionsByRole(String roleOid, Permission.Type type) {
        Criteria criteria = currentSession().createCriteria(Permission.class);
        criteria.add(Restrictions.eq("roleOid", roleOid));
        criteria.add(Restrictions.eq("type", type));
        List<Permission> permissions = list(criteria);
        for (Permission permission : permissions) {
            delete(permission);
        }
    }

    @Override
    public Set<? extends PermissionEntry> findByRoleId(String id) {
        return new HashSet<>(findByRoleOId(id));
    }
}
