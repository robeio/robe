package io.robe.admin.hibernate.dao;

import com.google.inject.Inject;
import io.robe.hibernate.dao.BaseDao;
import io.robe.quartz.hibernate.TriggerEntity;
import org.hibernate.SessionFactory;

/**
 * Created by sinanselimoglu on 31/03/14.
 */
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
}
