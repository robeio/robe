package io.robe.admin.resources;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.SystemParameterDao;
import io.robe.admin.hibernate.entity.SystemParameter;
import io.robe.admin.util.SystemParameterCache;
import io.robe.auth.Credentials;
import io.robe.common.exception.RobeRuntimeException;
import org.hibernate.FlushMode;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static org.hibernate.CacheMode.GET;

@Path("systemparameters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemParameterResource {

    @Inject
    private SystemParameterDao systemParameterDao;

    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<SystemParameter> getAll(@Auth Credentials credentials) {
        return systemParameterDao.findAll(SystemParameter.class);
    }

    @POST
    @UnitOfWork
    public SystemParameter create(@Auth Credentials credentials, @Valid SystemParameter systemParameter) {
        Optional<SystemParameter> parameter = systemParameterDao.findByKey(systemParameter.getKey());
        if (parameter.isPresent()) {
            throw new RobeRuntimeException("KEY", systemParameter.getKey() + " already used by another System Parameter. Please use different key.");
        }
        return systemParameterDao.create(systemParameter);
    }

    @PUT
    @Path("{id}")
    @UnitOfWork(flushMode = FlushMode.MANUAL)
    public SystemParameter update(@Auth Credentials credentials, @PathParam("id") String id, @Valid SystemParameter systemParameter) {
        systemParameter = systemParameterDao.update(systemParameter);
        try {
            systemParameterDao.flush();
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            throw new RobeRuntimeException("KEY", systemParameter.getKey() + " already used by another System Parameter. Please use different key.");
        }
        return systemParameter;
    }

    @DELETE
    @UnitOfWork
    @Path("{id}")
    public SystemParameter delete(@Auth Credentials credentials, @PathParam("id") String id, @Valid SystemParameter systemParameter) {
        return systemParameterDao.delete(systemParameter);
    }

    @POST
    @Path("clearcache")
    @UnitOfWork(readOnly = true, flushMode = FlushMode.MANUAL)
    public void clearCache() {
        SystemParameterCache.fillCache();
    }


}
