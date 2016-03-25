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
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("mailtemplates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MailTemplateResource {

    @Inject
    private MailTemplateDao mailTemplateDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET,flushMode = FlushMode.MANUAL)
    public List<MailTemplate> getAll() {
        return mailTemplateDao.findAll(MailTemplate.class);
    }

    @POST
    @UnitOfWork
    public MailTemplate create(@Auth Credentials credentials, @Valid MailTemplate mailTemplate) {
        return mailTemplateDao.create(mailTemplate);
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public MailTemplate update(@Auth Credentials credentials, @PathParam("id") String id, @Valid MailTemplate mailTemplate) {
        return mailTemplateDao.update(mailTemplate);
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public MailTemplate delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid MailTemplate mailTemplate) {
        return mailTemplateDao.delete(mailTemplate);
    }
}
