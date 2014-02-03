package io.robe.service;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.dao.ServiceDao;
import io.robe.hibernate.dao.UserDao;
import io.robe.hibernate.entity.Role;
import io.robe.hibernate.entity.Service;
import io.robe.hibernate.entity.User;
import org.reflections.Reflections;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Method;
import java.util.Set;


@Path("initialize")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InitializeResource {

	@Inject
	ServiceDao serviceDao;
	@Inject
	UserDao userDao;
	@Inject
	RoleDao roleDao;

	@UnitOfWork
	@POST
	public void execute() {
		Reflections reflections = new Reflections("io", this.getClass().getClassLoader());
		Set<Class<?>> services = reflections.getTypesAnnotatedWith(Path.class);
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
					Optional<Service> optional = serviceDao.findByPathAndMethod(path, Service.Method.valueOf(httpMethod));
					if (!optional.isPresent()) {
						Service entity = new io.robe.hibernate.entity.Service();
						entity.setPath(path);
						entity.setMethod(Service.Method.valueOf(httpMethod));
						serviceDao.create(entity);
					}

				}
			}
		}
		Optional<Role> roleOptional = roleDao.findByName("Admin");
		if (!roleOptional.isPresent()) {
			Role role = new Role();
			role.setCode("admin");
			role.setName("Admin");
			roleDao.create(role);
		}
		Optional<User> userOptional = userDao.findByEmail("admin@robe.io");
		if (!userOptional.isPresent()) {
			User user = new User();
			user.setEmail("admin@robe.io");
			user.setActive(true);
			user.setName("admin");
			user.setSurname("admin");
			user.setPassword("96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1e");
			user.setRole(roleDao.findByName("Admin").get());
			userDao.create(user);
		}

	}

	private boolean isItService(Method method) {
		return method.getAnnotation(GET.class) != null || method.getAnnotation(PUT.class) != null || method.getAnnotation(POST.class) != null || method.getAnnotation(DELETE.class) != null || method.getAnnotation(OPTIONS.class) != null;
	}
}
