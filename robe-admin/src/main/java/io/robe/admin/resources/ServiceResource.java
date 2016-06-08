package io.robe.admin.resources;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.PATCH;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.Credentials;
import io.robe.auth.RobeAuth;
import io.robe.auth.data.entry.ServiceEntry;
import io.robe.common.service.RobeService;
import io.robe.common.service.search.SearchParam;
import io.robe.common.service.search.model.SearchModel;
import io.robe.common.utils.FieldReflection;
import io.robe.guice.GuiceBundle;
import io.robe.guice.GuiceConfiguration;
import org.hibernate.FlushMode;
import org.reflections.Reflections;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.hibernate.CacheMode.GET;

@Path("services")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceResource {


    @Inject
    private ServiceDao serviceDao;

    /**
     * Returns all {@link Service ) as a collection.
     *
     * @param credentials Injected by {@link RobeAuth } annotation for authentication.
     * @return all {@link Service} as a collection.
     */
    @RobeService(group = "Service", description = "Returns all Services as a collection.")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Service> getAll(@RobeAuth Credentials credentials, @SearchParam SearchModel search) {
        return serviceDao.findAll(search);
    }

    /**
     * Return {@link Service) resource and matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     *
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Service}
     * @return {@link Service} resource matches with the given id.
     */
    @RobeService(group = "Service", description = "Return Service resource.")
    @Path("{id}")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public Service get(@RobeAuth Credentials credentials, @PathParam("id") String id) {
        Service entity = serviceDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return entity;
    }

    /**
     * Create {@link Service) resource and matches with the given id.
     *
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @param model       This is the one model of {@link Service}
     * @return Create {@link Service) resource and return given Service path link at header Location=example/{id].
     */
    @RobeService(group = "Service", description = "Create Service resource and return given Service path link at header Location=example/{id].")
    @POST
    @UnitOfWork
    public Service create(@RobeAuth Credentials credentials, @Valid Service model) {
        return serviceDao.create(model);
    }

    /**
     * Update {@link Service) resource and matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Service}
     * @param model       This is the one model of {@link Service}
     * @return Update {@link Service} resource and matches with the given id.
     */
    @RobeService(group = "Service", description = "Update Service resource and matches with the given id.")
    @Path("{id}")
    @PUT
    @UnitOfWork
    public Service update(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid Service model) {
        if (!id.equals((model.getOid()))) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Service entity = serviceDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return serviceDao.update(model);
    }

    /**
     * Update {@link Service) resource and matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Service}
     * @param model       This is the one model of {@link Service}
     * @return Update {@link Service) resource and matches with the given id.
     */
    @RobeService(group = "Service", description = "Update Service resource.")
    @PATCH
    @UnitOfWork
    @Path("{id}")
    public Service merge(@RobeAuth Credentials credentials, @PathParam("id") String id, Service model) {
        if (id.equals(model.getOid()))
            throw new WebApplicationException(Response.status(412).build());
        Service dest = serviceDao.findById(id);
        if (dest == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        FieldReflection.mergeRight(model, dest);
        return serviceDao.update(model);
    }

    /**
     * Delete {@link Service) resource and matches with the given id.
     * <p>
     * Status Code:
     * Not Found  404
     * Not Matches 412
     *
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @param id          This is  the oid of {@link Service}
     * @param model       This is the one model of {@link Service}
     * @return Delete {@link Service) resource.
     */
    @RobeService(group = "Service", description = "Delete Service resource.")
    @Path("{id}")
    @DELETE
    @UnitOfWork
    public Service delete(@RobeAuth Credentials credentials, @PathParam("id") String id, @Valid Service model) {

        if (!id.equals(model.getOid())) {
            throw new WebApplicationException(Response.status(412).build());
        }
        Service entity = serviceDao.findById(id);
        if (entity == null) {
            throw new WebApplicationException(Response.status(404).build());
        }
        return serviceDao.delete(entity);
    }


    /**
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @return collection of {@link Service} groups
     */
    @RobeService(group = "Permission", description = "Get all services group for permission")
    @Path("groups")
    @GET
    @UnitOfWork
    public List<Service> listGroups(@RobeAuth Credentials credentials) {
        return serviceDao.findServiceByGroups();
    }


    /**
     * refreshing service with description
     * <p>
     * TODO exception handler
     *
     * @param credentials Injected by {@link RobeAuth} annotation for authentication.
     * @return Response.OK
     */

    @Path("refresh")
    @GET
    @UnitOfWork
    public Response refreshServices(@RobeAuth Credentials credentials) {


        GuiceConfiguration configuration = GuiceBundle.getConfiguration();

        Reflections reflections = new Reflections(configuration.getScanPackages(), this.getClass().getClassLoader());
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Path.class);
        int count = 0;
        for (Class service : services) {
            String parentPath = "/" + ((Path) service.getAnnotation(Path.class)).value();
            for (Method method : service.getMethods()) {
                String httpMethod = ifServiceGetHttpMethod(method);
                if (httpMethod == null) {
                    continue;
                }
                String path = parentPath;
                path = extractPath(method, path);

                io.robe.admin.hibernate.entity.Service entity = serviceDao.findByPathAndMethod(path, ServiceEntry.Method.valueOf(httpMethod));
                RobeService robeService = (RobeService) method.getAnnotation(RobeService.class);

                if (entity != null) {
                    if (robeService != null) {
                        entity.setDescription(robeService.description());
                        entity.setGroup(robeService.group());
                        serviceDao.update(entity);
                    }
                    continue;
                }
                entity = new io.robe.admin.hibernate.entity.Service();
                entity.setPath(path);
                entity.setMethod(io.robe.admin.hibernate.entity.Service.Method.valueOf(httpMethod));
                if (robeService != null) {
                    entity.setDescription(robeService.description());
                    entity.setGroup(robeService.group());
                } else {
                    entity.setGroup("UNGROUPED");
                    entity.setDescription("");

                }
                entity.setDescription(entity.getDescription() + " (" + entity.getMethod() + " " + entity.getPath() + ")");
                serviceDao.create(entity);
                count++;

            }
        }
        return Response.ok(count).build();
    }

    private String extractPath(Method method, String path) {
        if (method.getAnnotation(Path.class) != null) {
            path += "/" + method.getAnnotation(Path.class).value();
            path = path.replaceAll("//", "/");
        }
        return path;
    }

    private String ifServiceGetHttpMethod(Method method) {
        if (method.getAnnotation(GET.class) != null)
            return "GET";
        if (method.getAnnotation(PUT.class) != null)
            return "PUT";
        if (method.getAnnotation(POST.class) != null)
            return "POST";
        if (method.getAnnotation(DELETE.class) != null)
            return "DELETE";
        if (method.getAnnotation(PATCH.class) != null)
            return "PATCH";
        if (method.getAnnotation(OPTIONS.class) != null)
            return "OPTIONS";

        return null;
    }
}
