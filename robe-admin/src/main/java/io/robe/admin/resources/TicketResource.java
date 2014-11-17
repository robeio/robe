package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.views.View;
import io.robe.admin.hibernate.dao.TicketDao;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.admin.view.ChangePasswordView;
import io.robe.admin.view.NotFoundView;
import io.robe.admin.view.RegisterView;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Calendar;

@Path("ticket")
@Consumes(MediaType.TEXT_HTML)
@Produces(MediaType.TEXT_HTML)
public class TicketResource {

    @Inject
    TicketDao ticketDao;

    @GET
    @Path("{ticketOid}")
    @UnitOfWork
    public View getView(@PathParam("ticketOid") String tickedOid, @Context UriInfo uriInfo) {

        String url = uriInfo.getBaseUri().toString();
        Ticket ticket = ticketDao.findById(tickedOid);
        if (ticket != null) {
            if (ticket.getExpirationDate().getTime() > Calendar.getInstance().getTime().getTime()) {
                if (ticket.getType().equals(Ticket.Type.CHANGE_PASSWORD)) {
                    return new ChangePasswordView(tickedOid, ticket.getUser().getUsername(), url);
                } else if (ticket.getType().equals(Ticket.Type.REGISTER)) {
                    return new RegisterView(ticket.getUser().getUsername(), tickedOid, url);
                }
            } else {
                return new NotFoundView("Your ticket expired");
            }

        }
        return new NotFoundView("Ticket not found.");
    }


}
