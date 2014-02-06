package io.robe.resources;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.dao.TicketDao;
import io.robe.hibernate.entity.Ticket;
import io.robe.view.ChangePasswordView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("ticket")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_HTML)
public class TicketResource {


    @Inject
    TicketDao ticketDao;
    @Inject
    RoleDao roleDao;

    @Path("{ticketOid}")
    @POST
    @UnitOfWork
    public void consumeTicket(@PathParam("ticketOid") String tickedOid) {
        Preconditions.checkNotNull(tickedOid);
        Ticket ticket = ticketDao.findById(tickedOid);
        Preconditions.checkNotNull(ticket);
    }

    @GET
    @Path("{ticketOid}")
    @UnitOfWork
    public ChangePasswordView getView(@PathParam("ticketOid") String tickedOid) {
        return new ChangePasswordView(tickedOid);
    }


}
