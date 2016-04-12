package io.robe.admin.hibernate.dao;

import javax.inject.Inject;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class QuartzTriggerDao extends BaseDao<TriggerEntity> {
    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public QuartzTriggerDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<TriggerEntity> findByJobOid(String jobOid) {
        Criteria criteria = currentSession().createCriteria(TriggerEntity.class);
        criteria.add(Restrictions.eq("jobOid", jobOid));
        return list(criteria);

    }
}
