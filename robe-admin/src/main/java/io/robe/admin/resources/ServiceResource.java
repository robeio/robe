package io.robe.admin.resources;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.hibernate.dao.RoleDao;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.entity.Service;
import io.robe.auth.Credentials;
import io.robe.auth.data.entry.ServiceEntry;
import org.reflections.Reflections;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;
import java.util.*;

@Path("service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceResource {

    @Inject
    private ServiceDao serviceDao;

    @Path("/all")
    @GET
    @UnitOfWork
    public List<Service> getAll(@Auth Credentials credentials) {

        return serviceDao.findAll(Service.class);
    }

    @Path("refresh")
    @GET
    @UnitOfWork
    public Response refreshServices(@Auth Credentials credentials) {

        Reflections reflections = new Reflections(new String[] {"io"}, this.getClass().getClassLoader());
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Path.class);
        int count=0;
        for (Class service : services) {
            String parentPath = "/" + ((Path) service.getAnnotation(Path.class)).value();
            for (Method method : service.getMethods()) {
                if (isItService(method)) {
                    String httpMethod = method.getAnnotation(GET.class) != null ? "GET" :
                            method.getAnnotation(POST.class) != null ? "POST" :
                                    method.getAnnotation(PUT.class) != null ? "PUT" :
                                            method.getAnnotation(DELETE.class) != null ? "DELETE" :
                                                    method.getAnnotation(OPTIONS.class) != null ? "OPTIONS" : "";
                    String path = parentPath;
                    if (method.getAnnotation(Path.class) != null) {
                        path += "/" + method.getAnnotation(Path.class).value();
                        path = path.replaceAll("//", "/");
                    }
                    io.robe.admin.hibernate.entity.Service entity =serviceDao.findByPathAndMethod(path, ServiceEntry.Method.valueOf(httpMethod));
                    if (entity == null) {
                        entity = new io.robe.admin.hibernate.entity.Service();
                        entity.setPath(path);
                        entity.setMethod(io.robe.admin.hibernate.entity.Service.Method.valueOf(httpMethod));
                        serviceDao.create(entity);
                        count++;
                    }

                }
            }
        }
        return Response.ok(count).build();
    }

    private boolean isItService(Method method) {
        return method.getAnnotation(GET.class) != null || method.getAnnotation(PUT.class) != null || method.getAnnotation(POST.class) != null || method.getAnnotation(DELETE.class) != null || method.getAnnotation(OPTIONS.class) != null;
    }
}
