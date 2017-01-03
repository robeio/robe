package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.HTriggerInfo;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;

public class TriggerDao extends BaseDao<HTriggerInfo> {
    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public TriggerDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<HTriggerInfo> findByJobOid(String jobOid) {
        Criteria criteria = currentSession().createCriteria(HTriggerInfo.class);
        criteria.add(Restrictions.eq("jobOid", jobOid));
        return list(criteria);

    }

    public HTriggerInfo findByJobOidAndName(String jobOid, String name) {
        Criteria criteria = currentSession().createCriteria(HTriggerInfo.class);
        criteria.add(Restrictions.eq("jobOid", jobOid));
        criteria.add(Restrictions.eq("name", name));
        return uniqueResult(criteria);
    }
}
