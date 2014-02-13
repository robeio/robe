package io.robe.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.dao.LanguageDao;
import io.robe.hibernate.entity.Language;
import io.robe.hibernate.entity.MailTemplate;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by kaanalkim on 13/02/14.
 */
@Path("language")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LanguageResource {

    @Inject
    LanguageDao languageDao;

    @GET
    @Path("/all")
    @UnitOfWork
    public List<Language> getAll() {
        List<Language> list = languageDao.findAll(Language.class);
        return list;
    }


}
