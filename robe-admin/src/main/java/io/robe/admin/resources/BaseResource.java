package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.FieldReflection;
import io.robe.hibernate.dao.BaseDao;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.FlushMode;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.CacheMode.GET;

public abstract class BaseResource<T extends BaseEntity> {

    private final Class<T> entity;
    @Inject
    private BaseDao<T> dao;

    @SuppressWarnings("unchecked")
    protected BaseResource() {
        this.entity = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<T> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {

        // TODO dao.findAll() is not working!!!
        return new ArrayList<T>();
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public T get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        return dao.findById(entity, id);
    }

    @POST
    @UnitOfWork
    public T create(@RobeAuth Credentials credentials, @Valid T model) {
        return dao.create(model);
    }

    @PUT
    @UnitOfWork
    @Path("{id}")
    public T update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid T model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        T entity = dao.findById(this.entity, id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        dao.detach(entity);

        return dao.update(model);
    }


    @PATCH
    @UnitOfWork
    @Path("{id}")
    public T merge(@RobeAuth Credentials credentials, @PathParam("id") String id, T model) {
        if (!id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        T dest = dao.findById(this.entity, id);
        if (dest == null)
            throw new WebApplicationException(Response.status(404).build());

        FieldReflection.mergeRight(model, dest);
        return dao.update(dest);
    }

    @DELETE
    @UnitOfWork
    @Path("{id}")
    public T delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid T model) {
        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        T entity = dao.findById(this.entity, id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return dao.delete(entity);
    }
}