package io.robe.admin.hibernate.dao;

import com.google.inject.Inject;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.hibernate.dao.BaseDao;
import org.hibernate.SessionFactory;

public class TicketDao extends BaseDao<Ticket> {

    @Inject
    public TicketDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
