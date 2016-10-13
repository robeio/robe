package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.quartz.hibernate.TriggerEntity;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.FieldReflection;
import org.hibernate.FlushMode;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;


@Path("triggers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriggerResource {

    @Inject
    private QuartzTriggerDao quartzTriggerDao;

    /**
     * Return all TriggerEntity as a collection
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link TriggerEntity} as a collection
     */
    @RobeService(group = "QuartzJob", description = "Returns all TriggerEntity as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<TriggerEntity> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {
        return quartzTriggerDao.findAll(search);
    }

    /**
     * Return a TriggerEntity resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link TriggerEntity}
     * @return a  {@link TriggerEntity} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Returns a TriggerEntity resource with the matches given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public TriggerEntity get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        TriggerEntity entity = quartzTriggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create a {@link TriggerEntity} resource.
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param model       This is the one model of {@link TriggerEntity}
     * @return create a {@link TriggerEntity} resource.
     */
    @RobeService(group = "QuartzJob", description = "Create a TriggerEntity resource.")
    @POST
    @UnitOfWork
    public TriggerEntity create(@RobeAuth Credentials credentials, @Valid TriggerEntity model) {
        return quartzTriggerDao.create(model);
    }

    /**
     * Update a TriggerEntity resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link TriggerEntity}
     * @param model       This is the one model of {@link TriggerEntity}
     * @return Update a  {@link TriggerEntity} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Update a TriggerEntity resource with the matches given id.")
    @PUT
    @UnitOfWork
    @Path("{id}")
    public TriggerEntity update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid TriggerEntity model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        TriggerEntity entity = quartzTriggerDao.findById(id);
        quartzTriggerDao.detach(entity);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }

        return quartzTriggerDao.update(model);
    }

    /**
     * Update a TriggerEntity resource with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link TriggerEntity}
     * @param model       This is the one model of {@link TriggerEntity}
     * @return Updates a  {@link TriggerEntity} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Update a TriggerEntity resource with the matches given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public TriggerEntity merge(@RobeAuth Credentials credentials, @PathParam("id") String id, TriggerEntity model) {
        if (!id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        TriggerEntity dest = quartzTriggerDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        quartzTriggerDao.detach(dest);
        FieldReflection.mergeRight(model, dest);
        return quartzTriggerDao.update(dest);
    }

    /**
     * Delete a TriggerEntity resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link TriggerEntity}
     * @param model       This is the one model of {@link TriggerEntity}
     * @return Delete a  {@link TriggerEntity} resource  with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Delete a TriggerEntity resource with the matches given id.")
    @DELETE
    @UnitOfWork
    @Path("{id}")
    public TriggerEntity delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid TriggerEntity model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        TriggerEntity entity = quartzTriggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return quartzTriggerDao.delete(entity);
    }

}
