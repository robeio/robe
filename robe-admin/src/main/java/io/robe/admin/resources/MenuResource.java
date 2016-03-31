package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.MenuDao;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.auth.Credentials;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("menus")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MenuResource {

    @Inject
    private MenuDao menuDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Menu> getAll() {
        return menuDao.findAll(Menu.class);
    }

    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Menu get(@Auth Credentials credentials, @PathParam("id") String id) {
        Menu entity = menuDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    @POST
    @UnitOfWork
    public Menu create(@Auth Credentials credentials, @Valid Menu model) {
        return menuDao.create(model);
    }

    @Path("{id}")
    @PUT
    @UnitOfWork
    public Menu update(@Auth Credentials credentials, @PathParam("id") String id, @Valid Menu model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Menu entity = menuDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return menuDao.update(model);
    }

    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Menu delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid Menu model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Menu entity = menuDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return menuDao.delete(entity);
    }
}
