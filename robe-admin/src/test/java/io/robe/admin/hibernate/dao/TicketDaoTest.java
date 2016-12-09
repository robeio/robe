package io.robe.admin.hibernate.dao;

import io.robe.admin.hibernate.entity.Ticket;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by hasanmumin on 12/10/2016.
 */
public class TicketDaoTest extends BaseDaoTest<Ticket, TicketDao> {
    @Override
    public Ticket instance() {
        Ticket ticket = new Ticket();
        ticket.setExpirationDate(new DateTime().plusDays(1).toDate());
        ticket.setType(Ticket.Type.ACTIVATE);
        ticket.setUserOid("1");
        return ticket;
    }

    @Override
    public Ticket update(Ticket model) {
        model.setExpirationDate(new DateTime().plusDays(2).toDate());
        model.setType(Ticket.Type.CHANGE_PASSWORD);
        return model;
    }

    @Test
    public void findByUserOidAndExpirationDate() {
        super.createFrom();
        Optional<Ticket> ticket = dao.findByUserOidAndExpirationDate("1");
        Assert.assertTrue(ticket.isPresent());
        super.deleteFrom(ticket.get());
    }
}
