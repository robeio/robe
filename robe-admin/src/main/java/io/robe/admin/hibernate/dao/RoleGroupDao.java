package io.robe.admin.hibernate.dao;

import javax.inject.Inject;
import io.robe.admin.hibernate.entity.RoleGroup;
import io.robe.auth.data.entry.RoleGroupEntry;
import io.robe.auth.data.store.RoleGroupStore;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RoleGroupDao extends BaseDao<RoleGroup> implements RoleGroupStore {
    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public RoleGroupDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<RoleGroup> findByGroupOId(String groupOid) {
        Criteria criteria = currentSession().createCriteria(RoleGroup.class);
        criteria.add(Restrictions.eq("groupOid", groupOid));
        return list(criteria);
    }

    @Override
    public Set<? extends RoleGroupEntry> findByGroupId(String groupOid) {
        return new HashSet<>(findByGroupOId(groupOid));
    }
}
