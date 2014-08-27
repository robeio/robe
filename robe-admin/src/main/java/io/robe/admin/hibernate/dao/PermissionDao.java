package io.robe.admin.hibernate.dao;

import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.admin.hibernate.entity.Role;
import io.robe.hibernate.dao.BaseDao;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class PermissionDao extends BaseDao<Permission> {

    @Inject
    public PermissionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Permission findByRoleAndItem(Role role, BaseEntity restrictedItem) {
        Criteria criteria = currentSession().createCriteria(Permission.class);
        criteria.add(Restrictions.eq("role", role));
        criteria.add(Restrictions.eq("restrictedItemOid", restrictedItem.getOid()));
        return uniqueResult(criteria);
    }

    public void deleteRestrictionsByRole(Role role, Permission.Type type) {
        Criteria criteria = currentSession().createCriteria(Permission.class);
        criteria.add(Restrictions.eq("role", role));
        criteria.add(Restrictions.eq("type", type));
        List<Permission> permissions = list(criteria);
        for (Permission permission : permissions) {
            delete(permission);
        }
    }

}
