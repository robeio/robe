package io.robe.resources;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.sun.jndi.toolkit.url.Uri;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.exception.RobeRuntimeException;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.dao.TicketDao;
import io.robe.hibernate.entity.Ticket;
import io.robe.hibernate.entity.User;
import io.robe.utils.HashingUtils;
import io.robe.view.ChangePasswordView;
import org.apache.commons.httpclient.util.URIUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

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
        //TODO HTML form must give @FormParam as Array and then we have to do change password according to ticketOid
        Preconditions.checkNotNull(tickedOid);
        Ticket ticket = ticketDao.findById(tickedOid);
        Preconditions.checkNotNull(ticket);
        Preconditions.checkNotNull(newPassword);
        Preconditions.checkNotNull(newPasswordConfirm);

        User user = ticket.getUser();

        newPassword = HashingUtils.hashSHA2(newPassword);
        newPasswordConfirm = HashingUtils.hashSHA2(newPasswordConfirm);
        String oldPassword = user.getPassword();

        if (newPassword.equals(newPasswordConfirm)) {
            //TODO if equal newPassword to oldpassword, password mustn't change
            if (newPassword.equals(oldPassword)) {

            } else {
                user.setPassword(HashingUtils.hashSHA2(newPassword));
            }
            return Response.seeOther(URI.create("http://127.0.0.1:8080/admin-ui/html/Workspace.html")).build();
        } else {
            throw new RobeRuntimeException("EE", "Yeni şifre doğrulanamadı.");
        }

    }

    @GET
    @Path("{ticketOid}")
    @UnitOfWork
    public ChangePasswordView getView(@PathParam("ticketOid") String tickedOid) {
        return new ChangePasswordView(tickedOid);
    }


}
