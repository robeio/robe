package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.auth.Credentials;
import io.robe.hibernate.dao.BaseDao;
import io.robe.hibernate.entity.BaseEntity;

import javax.validation.Valid;
import javax.ws.rs.*;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class BaseResource<T extends BaseEntity> {

    @Inject
    private BaseDao<T> dao;

    private final Class<T> entity;

    @SuppressWarnings("unchecked")
    protected BaseResource() {
        this.entity = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @GET
    @UnitOfWork
    public List<T> getAll(@Auth Credentials credentials) {
        return dao.findAll(entity);
    }

    @Path("{id}")
    @GET
    @UnitOfWork
    public T get(@Auth Credentials credentials, @PathParam("id") String id) {
        return dao.findById(entity, id);
    }

    @POST
    @UnitOfWork
    public T create(@Auth Credentials credentials, @Valid T model) {
        return dao.create(model);
    }

    @PUT
    @UnitOfWork
    @Path("{id}")
    public T update(@Auth Credentials credentials, @PathParam("id") String id, @Valid T model) {
        return dao.update(model);
    }


    @PATCH
    @UnitOfWork
    @Path("{id}")
    public T merge(@Auth Credentials credentials,@PathParam("id") String id, T model) {

        //TODO: copy model with out ID and null values.
        return dao.update(model);
    }

    @DELETE
    @UnitOfWork
    @Path("{id}")
    public T delete(@Auth Credentials credentials,@PathParam("id") String id, @Valid T model) {
        return dao.delete(model);
    }
}