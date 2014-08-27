package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.MailTemplateDao;
import io.robe.admin.hibernate.entity.MailTemplate;
import io.robe.auth.Credentials;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

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
    public MailTemplate createTemplate(@Auth Credentials credentials, Map<String, String> data) {
        String language = data.get("lang");
        String code = data.get("code");
        String template = data.get("template");

        checkNotNull(language, "tLang mustn't be null or empty");
        checkNotNull(template, "template mustn't be null or empty");
        checkNotNull(code, "tCode mustn't be null or empty");

        MailTemplate entity = new MailTemplate();
        entity.setLang(MailTemplate.Type.valueOf(language));


        entity.setCode(code);
        entity.setTemplate(template);
        mailTemplateDao.create(entity);

        return entity;
    }
}
