package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.ActionLog;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;

import javax.inject.Inject;

public class ActionLogDao extends BaseDao<ActionLog>  {

    @Inject
    public ActionLogDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
