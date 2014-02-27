package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.Role;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class RoleDao extends BaseDao<Role> {

    @Inject
    public RoleDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Role> findByName(String code) {
        Criteria criteria = currentSession().createCriteria(Role.class);
        criteria.add(Restrictions.eq("code", code));
        return Optional.fromNullable(uniqueResult(criteria));
    }
}