package io.robe.hibernate.dao;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.entity.Service;
import io.robe.hibernate.entity.User;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class ServiceDao extends BaseDao<Service> {

	@Inject
	public ServiceDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<Service> findByPathAndMethod(String path,Service.Method method) {
        Criteria criteria = currentSession().createCriteria(Service.class);
        criteria.add(Restrictions.eq("path", path));
        criteria.add(Restrictions.eq("method", method));
        return Optional.fromNullable(uniqueResult(criteria));
    }

}
