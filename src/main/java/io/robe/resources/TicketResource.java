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
import javax.ws.rs.core.Response;

@Path("ticket")
@Consumes(MediaType.TEXT_HTML)
@Produces(MediaType.TEXT_HTML)
public class TicketResource {

    @Inject
    TicketDao ticketDao;
    @Inject
    RoleDao roleDao;

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response consumeTicket(@FormParam("ticketOid") String tickedOid, @FormParam("newPassword") String newPassword, @FormParam("newPasswordConfirm") String newPasswordConfirm) {
//        Preconditions.checkNotNull(tickedOid);
//        Ticket ticket = ticketDao.findById(tickedOid);
//        Preconditions.checkNotNull(ticket);

        return Response.status(200).entity("ticketOid : " + tickedOid + " newPassword : " + newPassword + " newPasswordConfirm : " + newPasswordConfirm).build();
    }

    @GET
    @Path("{ticketOid}")
    @UnitOfWork
    public ChangePasswordView getView(@PathParam("ticketOid") String tickedOid) {
        return new ChangePasswordView(tickedOid);
    }


}
