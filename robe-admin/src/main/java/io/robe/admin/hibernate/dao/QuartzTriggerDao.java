package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;

public class QuartzTriggerDao extends BaseDao<HibernateTriggerInfo> {
    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public QuartzTriggerDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<HibernateTriggerInfo> findByJobOid(String jobOid) {
        Criteria criteria = currentSession().createCriteria(HibernateTriggerInfo.class);
        criteria.add(Restrictions.eq("jobOid", jobOid));
        return list(criteria);

    }

    public HibernateTriggerInfo findByJobOidAndName(String jobOid, String name) {
        Criteria criteria = currentSession().createCriteria(HibernateTriggerInfo.class);
        criteria.add(Restrictions.eq("jobOid", jobOid));
        criteria.add(Restrictions.eq("name", name));
        return uniqueResult(criteria);
    }
}
