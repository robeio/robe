package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.User;
import io.robe.auth.data.entry.UserEntry;
import io.robe.auth.data.store.UserStore;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class UserDao extends BaseDao<User> implements UserStore {

    @Inject
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<User> findByUsername(String username) {
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("email", username));
        return Optional.ofNullable(uniqueResult(criteria));
    }

    public List<User> findByRoleId(String roleId) {
        Criteria criteria = currentSession().createCriteria(User.class);
        criteria.add(Restrictions.eq("roleOid", roleId));
        return criteria.list();
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
