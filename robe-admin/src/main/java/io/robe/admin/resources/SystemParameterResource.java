package io.robe.admin.resources;

import javax.inject.Inject;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.SystemParameterDao;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.auth.Credentials;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.FieldReflection;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static org.hibernate.CacheMode.GET;

@Path("systemparameters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemParameterResource {

    @Inject
    private SystemParameterDao systemParameterDao;

    /**
     * Returns all {@link SystemParameter) as a collection.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @return all {@link SystemParameter} as a collection.
     */
    @RobeService(group = "SystemParameter", description = "Returns all SystemParameter as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<SystemParameter> getAll(@Auth Credentials credentials, @SearchParam SearchModel search) {
        return systemParameterDao.findAll(search);
    }

    /**
     * Return {@link SystemParameter) resource and matches with the given id.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link SystemParameter}
     * @return {@link SystemParameter} resource matches with the given id.
     */
    @RobeService(group = "SystemParameter", description = "Return SystemParameter resource.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = CacheMode.GET, flushMode = FlushMode.MANUAL)
    public SystemParameter get(@Auth Credentials credentials, @PathParam("id") String id) {

        SystemParameter entity = systemParameterDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create {@link SystemParameter) resource.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param model       This is the one model of {@link SystemParameter}
     * @return Create {@link SystemParameter) resource and return given SystemParameter path link at header Location=example/{id].
     */
    @RobeService(group = "SystemParameter", description = "Create SystemParameter resource and return given SystemParameter path link at header Location=example/{id].")
    @POST
    @UnitOfWork
    public SystemParameter create(@Auth Credentials credentials, @Valid SystemParameter model) {
        return systemParameterDao.create(model);
    }

    /**
     * Create or update {@link SystemParameter) resource.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param model       This is the one model of {@link SystemParameter}
     * @return Create {@link SystemParameter) resource and return given SystemParameter path link at header Location=example/{id].
     */
    @RobeService(group = "SystemParameter", description = "Create or update SystemParameter resource.")
    @POST
    @Path("admin")
    @UnitOfWork
    public Map<String, String> bulkSaveOrUpdate(Map<String, String> values) {

        for (Map.Entry<String, String> entry : values.entrySet()) {


            Optional<SystemParameter> parameterDao = systemParameterDao.findByKey(entry.getKey());

            SystemParameter parameter = null;

            if (!parameterDao.isPresent()) {
                parameter = new SystemParameter();
                parameter.setKey(entry.getKey());
                parameter.setValue(entry.getValue());
            } else {
                parameter=parameterDao.get();
                parameter.setValue(entry.getValue());
            }

            systemParameterDao.update(parameter);
        }


        return values;
    }

    /**
     * Update {@link SystemParameter) resource and matches with the given id.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link SystemParameter}
     * @param model       This is the one model of {@link SystemParameter}
     * @return Update {@link SystemParameter} resource and matches with the given id.
     */
    @RobeService(group = "SystemParameter", description = "Update SystemParameter resource and matches with the given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public SystemParameter update(@Auth Credentials credentials, @PathParam("id") String id, @Valid SystemParameter model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        SystemParameter entity = systemParameterDao.findById(id);
        systemParameterDao.detach(entity);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return systemParameterDao.update(model);
    }

    /**
     * Update {@link SystemParameter) resource.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link SystemParameter}
     * @param model       This is the one model of {@link SystemParameter}
     * @return Update {@link SystemParameter) resource and matches with the given id.
     */
    @RobeService(group = "SystemParameter", description = "Update SystemParameter resource.")
    @Path("{id}")
    @PATCH
    @UnitOfWork
    public SystemParameter merge(@Auth Credentials credentials, @PathParam("id") String id, SystemParameter model) {

        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        SystemParameter dest = systemParameterDao.findById(id);
        systemParameterDao.detach(dest);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return systemParameterDao.update(model);
    }

    /**
     * Delete {@link SystemParameter) resource.
     *
     * @param credentials Injected by {@link Auth} annotation for authentication.
     * @param id          This is  the oid of {@link SystemParameter}
     * @param model       This is the one model of {@link SystemParameter}
     * @return Delete {@link SystemParameter) resource.
     */
    @RobeService(group = "SystemParameter", description = "Delete SystemParameter resource.")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public SystemParameter delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid SystemParameter model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        SystemParameter entity = systemParameterDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return systemParameterDao.delete(entity);
    }

}
