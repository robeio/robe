package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.SystemParameterDao;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.admin.hibernate.entity.User;
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

import static org.hibernate.CacheMode.GET;

@Path("systemparameters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemParameterResource {

    @Inject
    private SystemParameterDao systemParameterDao;

    /**
     * Returns all SystemParameters as a collection with the related path.
     * @param credentials auto fill by {@link Auth} annotation for authentication.
     * @return all {@link SystemParameter} as a collection with the related path.
     */
    @RobeService(group = "SystemParameter", description = "Returns all SystemParameter as a collection with the related path.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<SystemParameter> getAll(@Auth Credentials credentials) {
        return systemParameterDao.findAll(SystemParameter.class);
    }
    /**
     * Return a single SystemParameter related with the path and matches with the given id.
     * @param credentials auto fill by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link SystemParameter}
     * @return A single SystemParameter related with the path and matches with the given id.
     */
    @RobeService(group = "SystemParameter", description = "Return a single SystemParameter related with the path.")
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
     * Creates a single SystemParameter related with the path.
     * @param credentials auto fill by {@link Auth} annotation for authentication.
     * @param model This is the one model of {@link SystemParameter}
     * @return Creates a single {@link SystemParameter) related with the path and returns given SystemParameter path link at header Location=example/{id].
     */
    @RobeService(group = "SystemParameter", description = "Creates a single SystemParameter related with the path and returns given SystemParameter path link at header Location=example/{id].")
    @POST
    @UnitOfWork
    public SystemParameter create(@Auth Credentials credentials, @Valid SystemParameter model) {
        return systemParameterDao.create(model);
    }

    /**
     *
     * @param credentials credentials auto fill by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link SystemParameter}
     * @param model This is the one model of {@link SystemParameter}
     * @return Updates a single {@link SystemParameter} related with the path and matches with the given id. Payload holds the whole data.
     */
    @RobeService(group = "SystemParameter", description = "Updates a single SystemParameter related with the path and matches with the given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork(flushMode = FlushMode.MANUAL)
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
     * @param credentials credentials auto fill by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link SystemParameter}
     * @param model This is the one model of {@link SystemParameter}
     * @return Updates a single {@link SystemParameter) related with the path and matches with the given id. Payload will only containe update data . Version of the SystemParameter can be available at ETag in an If-Match header.
     */
    @RobeService(group = "SystemParameter", description = "Updates a single SystemParameter related with the path.")
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
     *
     * @param credentials credentials auto fill by {@link Auth} annotation for authentication.
     * @param id This is  the oid of {@link SystemParameter}
     * @param model This is the one model of {@link SystemParameter}
     * @return Deletes a single {@link SystemParameter) for the related path.
     */
    @RobeService(group = "SystemParameter", description = "Deletes a single SystemParameter for the related path.")
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
