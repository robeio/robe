package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.HJobInfo;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.quartz.Job;

import javax.inject.Inject;

/**
 * Created by sinanselimoglu on 19/02/14.
 */
public class JobDao extends BaseDao<HJobInfo> {
    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public JobDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public HJobInfo findByJobClass(Class<? extends Job> jobClass) {
        return (HJobInfo) criteria()
                .add(Restrictions.eq("jobClass", jobClass))
                .uniqueResult();
    }

}
