package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.LanguageDao;
import io.robe.admin.hibernate.entity.Language;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
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

    /**
     * Returns all (@Link Language) as a collection.
     * @param credentials Injected by (@Link Auth) annotation for authentication.
     * @return all (@Link Language) as a collection.
     */
    @RobeService(group = "Language", description = "Returns all Languages as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public List<Language> getAll(@Auth Credentials credentials) {
        return languageDao.findAll(Language.class);
    }

    /**
     * Return {@link Language ) resource and matches with the given id.
     *  * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link Language}
     * @return {@link Language} resource matches with the given id.
     */
    @RobeService(group = "Language", description = "Return Language resource.")
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

    /**
     * Create {@link Language) resource and matches with the given id.
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param model This is the one model of {@link Language}
     * @return Create {@link Language) resource and return given Language path link at header Location=example/{id].
     */
    @RobeService(group = "Language", description = "Create Language resource and return given Language path link at header Location=example/{id].")
    @POST
    @UnitOfWork
    public Language create(@Auth Credentials credentials, @Valid Language model) {

        return languageDao.create(model);

    }

    /**
     * Update {@link Language) resource and matches with the given id.
     *  <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link Language}
     * @param model This is the one model of {@link Language}
     * @return Update {@link Language} resource and matches with the given id.
     */
    @RobeService(group = "Language", description = "Update Language resource and matches with the given id.")
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

    /**
     * Update {@link Language) resource and matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link Language}
     * @param model This is the one model of {@link Language}
     * @return Update {@link Language) resource and matches with the given id.
     */
    @RobeService(group = "Language", description = "Update Language resource.")
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

    /**
     * Delete {@link Language) resource and matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link Language}
     * @param model This is the one model of {@link Language}
     * @return Delete {@link Language) resource.
     */
    @RobeService(group = "Language", description = "Delete Language resource.")
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
