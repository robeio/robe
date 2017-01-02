package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;

import javax.inject.Inject;

/**
 * Created by sinanselimoglu on 19/02/14.
 */
public class QuartzJobDao extends BaseDao<HibernateJobInfo> {
    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public QuartzJobDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public HibernateJobInfo findByJobClass(Class<? extends Job> jobClass) {
        return (HibernateJobInfo) criteria()
                .add(Restrictions.eq("jobClass", jobClass))
                .uniqueResult();
    }

}
