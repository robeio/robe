package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.auth.Credentials;
import io.robe.hibernate.dao.BaseDao;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @Path("all")
    @GET
    @UnitOfWork
    public List<T> getAll(@Auth Credentials credentials) {
        return dao.findAll(entity);
    }

    @Path("{modelOid}")
    @GET
    @UnitOfWork
    public T get(@PathParam("modelOid") String modelOid, @Auth Credentials credentials) {
        return dao.findById(entity, modelOid);
    }

    @PUT
    @UnitOfWork
    public T create(@Valid T model, @Auth Credentials credentials) {
        return dao.create(model);
    }

    @POST
    @UnitOfWork
    public T update(@Valid T model, @Auth Credentials credentials) {
        return dao.update(model);
    }

    @DELETE
    @UnitOfWork
    public T delete(@Valid T model, @Auth Credentials credentials) {
        return dao.delete(model);
    }
}