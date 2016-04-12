package io.robe.admin.hibernate.dao;

import javax.inject.Inject;
import io.robe.admin.quartz.hibernate.JobEntity;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;

/**
 * Created by sinanselimoglu on 19/02/14.
 */
public class QuartzJobDao extends BaseDao<JobEntity> {
    /**
     * Constructor with session factory injection by guice
     *
     * @param sessionFactory injected session factory
     */
    @Inject
    public QuartzJobDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
