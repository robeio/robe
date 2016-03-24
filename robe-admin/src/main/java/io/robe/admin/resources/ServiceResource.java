package io.robe.admin.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.Credentials;
import io.robe.auth.data.entry.ServiceEntry;
import io.robe.common.service.RobeService;
import io.robe.guice.GuiceBundle;
import io.robe.guice.GuiceConfiguration;
import org.hibernate.FlushMode;
import org.reflections.Reflections;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.hibernate.CacheMode.GET;

@Path("service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceResource {


    @Inject
    private ServiceDao serviceDao;

    @Path("/all")
    @GET
    @UnitOfWork(readOnly = true, cacheMode = GET, flushMode = FlushMode.MANUAL)
    public List<Service> getAll(@Auth Credentials credentials) {

        return serviceDao.findAll(Service.class);
    }

    @Path("refresh")
    @GET
    @UnitOfWork
    public Response refreshServices(@Auth Credentials credentials) {


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
                }
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
        if (method.getAnnotation(OPTIONS.class) != null)
            return "OPTIONS";

        return null;
    }
}
