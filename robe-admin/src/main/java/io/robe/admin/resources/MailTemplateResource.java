package io.robe.admin.resources;

import javax.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.MailTemplateDao;
import io.robe.admin.hibernate.entity.MailTemplate;
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

@Path("mailtemplates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MailTemplateResource {

    @Inject
    private MailTemplateDao mailTemplateDao;

    /**
     * Returns all {@link MailTemplate} as a collection.
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @return all {@link MailTemplate} as a collection.
     */
    @RobeService(group = "MailTemplate", description = "Returns all MailTemplate as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<MailTemplate> getAll(@Auth Credentials credentials, @SearchParam SearchModel search) {
        return mailTemplateDao.findAll(search);
    }

    /**
     * Return {@link MailTemplate} resource and matches with the given id.
     * @param credentials Injected by (@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @return {@link MailTemplate} resource and matches with the given id.
     */
    @RobeService(group = "MailTemplate", description = "Return MailTemplate resource and matches with the given id.")
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
     * Create {@link MailTemplate} resource.
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param model       This is the one model of {@link MailTemplate}
     * @return Create {@link MailTemplate} resource.
     */
    @RobeService(group = "MailTemplate", description = "Create MailTemplate resource.")
    @POST
    @UnitOfWork
    public MailTemplate create(@Auth Credentials credentials, @Valid MailTemplate model) {
        return mailTemplateDao.create(model);
    }

    /**
     * Update {@link MailTemplate} resource and matches with the given id.
     * @param credentials Injected by @{@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @param model       This is the one model of {@link MailTemplate}
     * @return Update {@link MailTemplate} resource and matches with the given id.
     */
    @RobeService(group = "MailTemplate", description = "Update MailTemplate resource and matches with the given id.")
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
     * Update {@link MailTemplate} resource and matches with the given id.
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @param model       This is the one model of {@link MailTemplate}
     * @return Update {@link MailTemplate} resource and matches with the given id.
     */
    @RobeService(group = "MailTemplate", description = "Update MailTemplate resource and matches with the given id.")
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
     * Delete {@link MailTemplate} resource.
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link MailTemplate}
     * @param model       This is the one model of {@link MailTemplate}
     * @return Delete {@link MailTemplate} resource.
     */
    @RobeService(group = "MailTemplate", description = "Delete MailTemplate resource.")
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
