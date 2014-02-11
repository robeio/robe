package io.robe.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.auth.Credentials;
import io.robe.dto.BasicPair;
import io.robe.hibernate.dao.MailTemplateDao;
import io.robe.hibernate.entity.MailTemplate;

import javax.sql.rowset.serial.SerialBlob;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

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

    @PUT
    @UnitOfWork
    public HashMap createTemplate(@Auth Credentials credentials, HashMap<String, String> data) {
        String tLang = data.get("tLang");
        String template = data.get("template");

        checkNotNull(tLang, "tLang mustn't be null or empty");
        checkNotNull(template, "template mustn't be null or empty");

        MailTemplate mailTemplate = new MailTemplate();
        if (tLang.equals("tr")) {
            mailTemplate.setLang(MailTemplate.Type.TR);
        } else if (tLang.equals("en")) {
            mailTemplate.setLang(MailTemplate.Type.EN);
        }

        byte[] byteTemplate = template.getBytes();
        try {
            Blob blobTemplate = new SerialBlob(byteTemplate);
            mailTemplate.setTemplate(blobTemplate);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mailTemplateDao.create(mailTemplate);

        return data;
    }

}
