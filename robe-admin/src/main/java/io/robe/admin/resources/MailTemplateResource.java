package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.MailTemplateDao;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.auth.Credentials;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("mailtemplate")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MailTemplateResource {
    @Inject
    MailTemplateDao mailTemplateDao;

    @GET
    @Path("/all")
    @UnitOfWork
    public List<MailTemplate> getAll() {
        return mailTemplateDao.findAll(MailTemplate.class);
    }

    @PUT
    @UnitOfWork
    public MailTemplate createTemplate(@Auth Credentials credentials, @Valid MailTemplate mailTemplate) {
        return mailTemplateDao.create(mailTemplate);
    }

    @DELETE
    @UnitOfWork
    public MailTemplate deleteTemplate(@Auth Credentials credentials, @Valid MailTemplate mailTemplate) {
        return mailTemplateDao.delete(mailTemplate);
    }

    @POST
    @UnitOfWork
    public MailTemplate updateTemplate(@Auth Credentials credentials, @Valid MailTemplate mailTemplate) {
        return mailTemplateDao.update(mailTemplate);
    }

}
