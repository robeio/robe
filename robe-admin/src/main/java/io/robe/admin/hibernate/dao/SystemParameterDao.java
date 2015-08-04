package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class SystemParameterDao extends BaseDao<SystemParameter> {

    @Inject
    public SystemParameterDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Optional<SystemParameter> findByKey(String key) {
        Criteria criteria = currentSession().createCriteria(SystemParameter.class);
        criteria.add(Restrictions.eq("key", key));
        return Optional.fromNullable(uniqueResult(criteria));
    }

}
