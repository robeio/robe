package io.robe.hibernate.dao;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.robe.hibernate.entity.User;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class UserDao extends BaseDao<User> {

	@Inject
	public UserDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public Optional<User> findByEmail(String email) {
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("email", email));
        return Optional.fromNullable(uniqueResult(criteria));
    }

    public Optional<User> getByApiKey(String api) {
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("api", api));
        return Optional.fromNullable(uniqueResult(criteria));
    }
}
