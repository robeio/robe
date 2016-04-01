package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.LanguageDao;
import io.robe.admin.hibernate.entity.Language;
import io.robe.auth.Credentials;
import io.robe.common.utils.FieldReflection;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("languages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LanguageResource {

    @Inject
    private LanguageDao languageDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public List<Language> getAll(@Auth Credentials credentials) {
        return languageDao.findAll(Language.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public Language get(@Auth Credentials credentials, @PathParam("id") String id) {

        Language entity = languageDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public Language create(@Auth Credentials credentials, @Valid Language model) {

        return languageDao.create(model);

    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public Language update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Language model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Language entity = languageDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return languageDao.update(model);
    }

    @Path("{id}")
    @PATCH
    @UnitOfWork
    public Language merge(@Auth Credentials credentials, @PathParam("id") String id, Language model) {

        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Language dest = languageDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return languageDao.update(model);
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Language delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Language model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Language entity = languageDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return languageDao.delete(model);
    }

}
