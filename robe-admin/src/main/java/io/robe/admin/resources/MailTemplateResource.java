package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.MailTemplateDao;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("mailtemplates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MailTemplateResource {

    @Inject
    private MailTemplateDao mailTemplateDao;

    /**
     * Returns all {@link MailTemplate} as a collection with the related path.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @return all @{@link MailTemplate} as a collection with the related path.
     */
    @RobeService(group = "MailTemplate", description = "Returns all MailTemplate as a collection")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<MailTemplate> getAll(@Auth Credentials credentials) {
        return mailTemplateDao.findAll(MailTemplate.class);
    }

    /**
     * Returns a single {@link MailTemplate} related with the path and matches with the given id
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @return a single @{@link MailTemplate} related with the path and matches with the given id.
     */
    @RobeService(group = "MailTemplate", description = "Returns a single MailTemplate related with the path and matches with the given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public MailTemplate get(@Auth Credentials credentials, @PathParam("id") String id) {
        MailTemplate entity = mailTemplateDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Creates a single {@link MailTemplate} related with the path.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param model       This is the one model of {@link MailTemplate}
     * @return Creates a single @{@link MailTemplate} related with the path
     */
    @RobeService(group = "MailTemplate", description = "Creates a single MailTemplate related with the path")
    @POST
    @UnitOfWork
    public MailTemplate create(@Auth Credentials credentials, @Valid MailTemplate model) {
        return mailTemplateDao.create(model);
    }

    /**
     * Updates a single {@link MailTemplate} related with the path and matches with the given id. Payload holds the whole data.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @param model       This is the one model of {@link MailTemplate}
     * @return Updates a single @{@link MailTemplate} related with the path and matches with the given id.
     */
    @RobeService(group = "MailTemplate", description = "Updates a single MailTemplate related with the path and matches with the given id")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public MailTemplate update(@Auth Credentials credentials, @PathParam("id") String id, @Valid MailTemplate model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        MailTemplate entity = mailTemplateDao.findById(id);
        mailTemplateDao.detach(entity);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return mailTemplateDao.update(model);
    }

    /**
     * Updates a single {@link MailTemplate} related with the path and matches with the given id.
     * Payload will only contains update data.
     * Version of the {@link MailTemplate} can be available at ETag in an If-Match header.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @param model       This is the one model of {@link MailTemplate}
     * @return Updates a single @{@link MailTemplate} related with the path and matches with the given id.
     */
    @RobeService(group = "MailTemplate", description = "Updates a single @{@link MailTemplate} related with the path and matches with the given id")
    @Path("{id}")
    @PATCH
    @UnitOfWork
    public MailTemplate merge(@Auth Credentials credentials, @PathParam("id") String id, MailTemplate model) {

        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());

        MailTemplate dest = mailTemplateDao.findById(id);
        mailTemplateDao.detach(dest);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return mailTemplateDao.update(model);
    }

    /**
     * Delete a single {@link MailTemplate} for the related path.
     *
     * @param credentials auto fill by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @param model       This is the one model of {@link MailTemplate}
     * @return deletes a single @{@link MailTemplate} for the related path
     */
    @RobeService(group = "MailTemplate", description = "Deletes a single MailTemplate for the related path")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public MailTemplate delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid MailTemplate model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        MailTemplate entity = mailTemplateDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return mailTemplateDao.delete(entity);
    }

}
