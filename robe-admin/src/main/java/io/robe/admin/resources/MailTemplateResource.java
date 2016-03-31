package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.MailTemplateDao;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.auth.Credentials;
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

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<MailTemplate> getAll() {
        return mailTemplateDao.findAll(MailTemplate.class);
    }

    @Path("(id)")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public MailTemplate get(@Auth Credentials credentials, @PathParam("id") String id) {
        MailTemplate entity = mailTemplateDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public MailTemplate create(@Auth Credentials credentials, @Valid MailTemplate model) {
        return mailTemplateDao.create(model);
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public MailTemplate update(@Auth Credentials credentials, @PathParam("id") String id, @Valid MailTemplate model) {

        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        MailTemplate entity = mailTemplateDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return mailTemplateDao.update(model);
    }

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
