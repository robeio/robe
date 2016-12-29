package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.reflection.Fields;
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
     * Return all HibernateTriggerInfo as a collection
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateTriggerInfo} as a collection
     */
    @RobeService(group = "QuartzJob", description = "Returns all HibernateTriggerInfo as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<HibernateTriggerInfo> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {
        return quartzTriggerDao.findAllStrict(search);
    }

    /**
     * Return a HibernateTriggerInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateTriggerInfo}
     * @return a  {@link HibernateTriggerInfo} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Returns a HibernateTriggerInfo resource with the matches given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public HibernateTriggerInfo get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateTriggerInfo entity = quartzTriggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create a {@link HibernateTriggerInfo} resource.
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param model       This is the one model of {@link HibernateTriggerInfo}
     * @return create a {@link HibernateTriggerInfo} resource.
     */
    @RobeService(group = "QuartzJob", description = "Create a HibernateTriggerInfo resource.")
    @POST
    @UnitOfWork
    public HibernateTriggerInfo create(@RobeAuth Credentials credentials, @Valid HibernateTriggerInfo model) {
        return quartzTriggerDao.create(model);
    }

    /**
     * Update a HibernateTriggerInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateTriggerInfo}
     * @param model       This is the one model of {@link HibernateTriggerInfo}
     * @return Update a  {@link HibernateTriggerInfo} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Update a HibernateTriggerInfo resource with the matches given id.")
    @PUT
    @UnitOfWork
    @Path("{id}")
    public HibernateTriggerInfo update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HibernateTriggerInfo model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        HibernateTriggerInfo entity = quartzTriggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        quartzTriggerDao.detach(entity);
        return quartzTriggerDao.update(model);
    }

    /**
     * Update a HibernateTriggerInfo resource with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateTriggerInfo}
     * @param model       This is the one model of {@link HibernateTriggerInfo}
     * @return Updates a  {@link HibernateTriggerInfo} resource with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Update a HibernateTriggerInfo resource with the matches given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public HibernateTriggerInfo merge(@RobeAuth Credentials credentials, @PathParam("id") String id, HibernateTriggerInfo model) {
        if (!id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        HibernateTriggerInfo dest = quartzTriggerDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        Fields.mergeRight(model, dest);
        return quartzTriggerDao.update(dest);
    }

    /**
     * Delete a HibernateTriggerInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateTriggerInfo}
     * @param model       This is the one model of {@link HibernateTriggerInfo}
     * @return Delete a  {@link HibernateTriggerInfo} resource  with the matches given id.
     */
    @RobeService(group = "QuartzJob", description = "Delete a HibernateTriggerInfo resource with the matches given id.")
    @DELETE
    @UnitOfWork
    @Path("{id}")
    public HibernateTriggerInfo delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HibernateTriggerInfo model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        HibernateTriggerInfo entity = quartzTriggerDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return quartzTriggerDao.delete(entity);
    }

}
