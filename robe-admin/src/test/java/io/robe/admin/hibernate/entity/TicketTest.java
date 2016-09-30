package io.robe.admin.hibernate.entity;

import org.junit.FixMethodOrder;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by recep on 30/09/16.
 */
@FixMethodOrder
public class TicketTest {
    Ticket entity = new Ticket();

    @Test
    public void getUserOid() throws Exception {
        entity.setUserOid("12345678");
        assertEquals("12345678", entity.getUserOid());
    }

    @Test
    public void getType() throws Exception {
        entity.setType(Ticket.Type.CHANGE_PASSWORD);
        assertEquals(Ticket.Type.CHANGE_PASSWORD, entity.getType());

        entity.setType(Ticket.Type.INIT_PASSWORD);
        assertEquals(Ticket.Type.INIT_PASSWORD, entity.getType());

        entity.setType(Ticket.Type.ACTIVATE);
        assertEquals(Ticket.Type.ACTIVATE, entity.getType());

        entity.setType(Ticket.Type.FORGOT_PASSWORD);
        assertEquals(Ticket.Type.FORGOT_PASSWORD, entity.getType());

        entity.setType(Ticket.Type.REGISTER);
        assertEquals(Ticket.Type.REGISTER, entity.getType());
    }

    @Test
    public void getExpirationDate() throws Exception {
        Date date = new Date();
        entity.setExpirationDate(date);
        assertEquals(date.getTime(), entity.getExpirationDate().getTime());
    }

}
