package io.robe.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.auth.Credentials;
import io.robe.hibernate.dao.MailTemplateDao;
import io.robe.hibernate.entity.MailTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by kaanalkim on 11/02/14.
 */
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
        List<MailTemplate> list = mailTemplateDao.findAll(MailTemplate.class);
        return list;
    }

    @PUT
    @UnitOfWork
    public MailTemplate createTemplate(@Auth Credentials credentials, HashMap<String, String> data) {
        String language = data.get("lang");
        String code = data.get("code");
        String template = data.get("template");

        checkNotNull(language, "tLang mustn't be null or empty");
        checkNotNull(template, "template mustn't be null or empty");
        checkNotNull(code, "tCode mustn't be null or empty");

        MailTemplate entity = new MailTemplate();
        if (language.equals("TR")) {
            entity.setLang(MailTemplate.Type.TR);
        } else if (language.equals("EN")) {
            entity.setLang(MailTemplate.Type.EN);
        }

        entity.setCode(code);
        entity.setTemplate(template);
        mailTemplateDao.create(entity);

        return entity;
    }

//    @POST
//    @UnitOfWork
//    public HashMap updateTemplate(@Auth Credentials credentials, HashMap<String, String> data) {
//        String tLang = data.get("tLang");
//        String template = data.get("template");
//        String tCode = data.get("tCode");
//
//        checkNotNull(tLang, "tLang mustn't be null or empty");
//        checkNotNull(template, "template mustn't be null or empty");
//        checkNotNull(tCode, "tCode mustn't be null or empty");
//
//        MailTemplate mailTemplate = new MailTemplate();
//
//
//
//
//    }

}
