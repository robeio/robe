package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.TicketDao;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.auth.Credentials;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("tickets")
@Consumes(MediaType.TEXT_HTML)
@Produces(MediaType.TEXT_HTML)
public class TicketResource {

    @Inject
    private TicketDao ticketDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Ticket> getAll() {
        return ticketDao.findAll(Ticket.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Ticket get(@Auth Credentials credentials, @PathParam("id") String id) {
        Ticket entity = ticketDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public Ticket create(@Auth Credentials credentials, @Valid Ticket model) {
        return ticketDao.create(model);
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public Ticket update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Ticket model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Ticket entity = ticketDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return ticketDao.update(model);
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Ticket delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Ticket model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Ticket entity = ticketDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return ticketDao.delete(entity);
    }

}
