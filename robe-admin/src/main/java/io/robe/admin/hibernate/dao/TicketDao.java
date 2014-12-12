package io.robe.admin.hibernate.dao;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.admin.hibernate.entity.User;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

public class TicketDao extends BaseDao<Ticket> {

    @Inject
    public TicketDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Optional<Ticket> findByUserAndActive(User user) {
        Criteria criteria = currentSession().createCriteria(Ticket.class);
        criteria.add(Restrictions.eq("user", user));
        criteria.add(Restrictions.ge("expirationDate", DateTime.now().toDate()));
        return Optional.fromNullable(uniqueResult(criteria));
    }

}
