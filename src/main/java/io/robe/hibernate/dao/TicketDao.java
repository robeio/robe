package io.robe.hibernate.dao;

import com.google.inject.Inject;
import io.robe.hibernate.entity.Ticket;
import org.hibernate.SessionFactory;

public class TicketDao extends BaseDao<Ticket> {

    @Inject
    public TicketDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

}
