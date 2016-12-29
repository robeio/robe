package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.QuartzJobDao;
import io.robe.admin.hibernate.dao.QuartzTriggerDao;
import io.robe.admin.hibernate.entity.HibernateJobInfo;
import io.robe.admin.hibernate.entity.HibernateTriggerInfo;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.reflection.Fields;
import io.robe.quartz.QuartzBundle;
import io.robe.quartz.info.JobInfo;
import org.hibernate.FlushMode;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("quartzjobs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QuartzJobResource {


    @Inject
    private QuartzTriggerDao quartzTriggerDao;

    @Inject
    private QuartzJobDao quartzJobDao;


    /**
     * Returns all HibernateTriggerInfo as a collection with the matches given job id.
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateTriggerInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateTriggerInfo as a collection with the matches given job id.")
    @GET
    @Path("{id}/triggers")
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<HibernateTriggerInfo> getJobTriggers(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        return quartzTriggerDao.findByJobOid(id);
    }


    /**
     * Return all HibernateJobInfo as a collection
     *
     * @param credentials auto fill by {@link RobeAuth} annotation for authentication.
     * @return all {@link HibernateJobInfo} as a collection
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns all HibernateJobInfo as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Collection<HibernateJobInfo> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {

        return quartzJobDao.findAllStrict(search);
    }

    /**
     * Return a HibernateJobInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateJobInfo}
     * @return a  {@link HibernateJobInfo} resource with the matches given id.
     */
    @RobeService(group = "HibernateJobInfo", description = "Returns a HibernateJobInfo resource with the matches given id.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public HibernateJobInfo get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        HibernateJobInfo entity = quartzJobDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create a {@link HibernateJobInfo} resource.
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param model       This is the one model of {@link HibernateJobInfo}
     * @return create a {@link HibernateJobInfo} resource.
     */
    @RobeService(group = "HibernateJobInfo", description = "Create a HibernateJobInfo resource.")
    @POST
    @UnitOfWork
    public HibernateJobInfo create(@RobeAuth Credentials credentials, @Valid HibernateJobInfo model) {
        return quartzJobDao.create(model);
    }

    /**
     * Update a HibernateJobInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateJobInfo}
     * @param model       This is the one model of {@link HibernateJobInfo}
     * @return Update a  {@link HibernateJobInfo} resource with the matches given id.
     */
    @RobeService(group = "HibernateJobInfo", description = "Update a HibernateJobInfo resource with the matches given id.")
    @PUT
    @UnitOfWork
    @Path("{id}")
    public HibernateJobInfo update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HibernateJobInfo model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        HibernateJobInfo entity = quartzJobDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        quartzJobDao.detach(entity);
        return quartzJobDao.update(model);
    }

    /**
     * Update a HibernateJobInfo resource with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateJobInfo}
     * @param model       This is the one model of {@link HibernateJobInfo}
     * @return Updates a  {@link HibernateJobInfo} resource with the matches given id.
     */
    @RobeService(group = "HibernateJobInfo", description = "Update a HibernateJobInfo resource with the matches given id.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public HibernateJobInfo merge(@RobeAuth Credentials credentials, @PathParam("id") String id, HibernateJobInfo model) {
        if (!id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        HibernateJobInfo dest = quartzJobDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        Fields.mergeRight(model, dest);
        return quartzJobDao.update(dest);
    }

    /**
     * Delete a HibernateJobInfo resource  with the matches given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials auto fill by @{@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link HibernateJobInfo}
     * @param model       This is the one model of {@link HibernateJobInfo}
     * @return Delete a  {@link HibernateJobInfo} resource  with the matches given id.
     */
    @RobeService(group = "HibernateJobInfo", description = "Delete a HibernateJobInfo resource with the matches given id.")
    @DELETE
    @UnitOfWork
    @Path("{id}")
    public HibernateJobInfo delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid HibernateJobInfo model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        HibernateJobInfo entity = quartzJobDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return quartzJobDao.delete(entity);
    }
}
