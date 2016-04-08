package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.TicketDao;
import io.robe.admin.hibernate.entity.Ticket;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
import io.robe.common.service.SearchParam;
import io.robe.common.service.jersey.model.SearchModel;
import io.robe.common.utils.FieldReflection;
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

    /**
     * Return all {@link Ticket}s as a collection.
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @return all @{@link Ticket}s as a collection.
     */
    @RobeService(group = "QuartzJob", description = "Return all tickets as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Ticket> getAll(@Auth Credentials credentials, @SearchParam SearchModel search) {
        return ticketDao.findAll(search);
    }

    /**
     * Returns a {@link Ticket} resource with the given id
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Ticket}
     * @return a @{@link Ticket} resource macthes with the given id.
     */
    @RobeService(group = "QuartzJob", description = "Returns a ticket resource with the given id")
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

    /**
     * Creates a {@link Ticket} resource.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param model       Data of {@link Ticket}
     * @return Creates a @{@link Ticket} resource.
     */
    @RobeService(group = "QuartzJob", description = "Creates a ticket resource.")
    @POST
    @UnitOfWork
    public Ticket create(@Auth Credentials credentials, @Valid Ticket model) {
        return ticketDao.create(model);
    }

    /**
     * Updates a {@link Ticket} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Ticket}
     * @param model       Data of {@link Ticket}
     * @return Updates a @{@link Ticket} resource matches with the given id.
     */
    @RobeService(group = "QuartzJob", description = "Updates a ticket resource matches with the given id.")
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

    /**
     * Updates a {@link Ticket} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Ticket}
     * @param model       Data of {@link Ticket}
     * @return Updates a @{@link Ticket} resource matches with the given id.
     */
    @RobeService(group = "QuartzJob", description = "Updates a ticket resource matches with the given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public Ticket merge(@Auth Credentials credentials, @PathParam("id") String id, Ticket model) {
        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Ticket dest = ticketDao.findById(id);
        if (ticketDao == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return ticketDao.update(model);
    }

    /**
     * Deletes a {@link Ticket} resource matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link Ticket}
     * @param model       Data of {@link Ticket}
     * @return deletes a @{@link Ticket} resource matches with the given id.
     */
    @RobeService(group = "QuartzJob", description = "Deletes a ticket resource matches with the given id.")
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
