package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.User;
import io.robe.auth.data.entry.UserEntry;
import io.robe.auth.data.store.UserStore;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class UserDao extends BaseDao<User> implements UserStore {

    @Inject
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<User> findByUsername(String username) {
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("email", username));
        return Optional.fromNullable(uniqueResult(criteria));
    }

    @Override
    public Optional<? extends UserEntry> changePassword(String username, String newPassword) {
        Optional<User> user = findByUsername(username);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            persist(user.get());
        }
        return user;
    }
}
