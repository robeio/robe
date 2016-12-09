package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.Ticket;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import javax.inject.Inject;
import java.util.Optional;

public class TicketDao extends BaseDao<Ticket> {

    @Inject
    public TicketDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public Optional<Ticket> findByUserOidAndExpirationDate(String userOid) {
        Criteria criteria = currentSession().createCriteria(Ticket.class);
        criteria.add(Restrictions.eq("userOid", userOid));
        criteria.add(Restrictions.ge("expirationDate", DateTime.now().toDate()));
        return Optional.ofNullable(uniqueResult(criteria));
    }

}
