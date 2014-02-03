package io.robe.cli;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.EnvironmentCommand;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.HibernateBundle;
import io.robe.hibernate.entity.Permission;
import io.robe.hibernate.entity.Role;
import io.robe.hibernate.entity.User;
import io.robe.service.RobeServiceConfiguration;
import net.sourceforge.argparse4j.inf.Namespace;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.Set;


public class InitializeCommand<T extends RobeServiceConfiguration> extends EnvironmentCommand<T> {

	private HibernateBundle hibernateBundle;


	public InitializeCommand(Service<T> service, HibernateBundle hibernateBundle) {
		super(service, "initialize", "Runs Hibernate and initialize required columns");
		this.hibernateBundle = hibernateBundle;

	}


	@Override
	@UnitOfWork
	protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {

		final Logger logger = LoggerFactory.getLogger(InitializeCommand.class);

		execute();


	}


	@UnitOfWork
	public void execute() {
		final Session session = hibernateBundle.getSessionFactory().openSession();

		Role role = (Role) session.createCriteria(Role.class).add(Restrictions.eq("name", "Admin")).uniqueResult();
		if (role == null) {
			role = new Role();
			role.setCode("admin");
			role.setName("Admin");
			session.persist(role);
		}

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
					io.robe.hibernate.entity.Service entity =
							(io.robe.hibernate.entity.Service) session.createCriteria(io.robe.hibernate.entity.Service.class)
									.add(Restrictions.eq("path", path))
									.add(Restrictions.eq("method", io.robe.hibernate.entity.Service.Method.valueOf(httpMethod)))
									.uniqueResult();
					if (entity == null) {
						entity = new io.robe.hibernate.entity.Service();
						entity.setPath(path);
						entity.setMethod(io.robe.hibernate.entity.Service.Method.valueOf(httpMethod));
						session.persist(entity);

						Permission permission = new Permission();
						permission.setpLevel((short) 7);
						permission.setType(Permission.Type.SERVICE);
						permission.setRestrictedItemOid(entity.getOid());
						permission.setRole(role);
						session.persist(permission);
					}

				}
			}
		}

		User user = (User) session.createCriteria(User.class).add(Restrictions.eq("email", "admin@robe.io")).uniqueResult();
		if (user == null) {
			user = new User();
			user.setEmail("admin@robe.io");
			user.setActive(true);
			user.setName("admin");
			user.setSurname("admin");
			user.setPassword("96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1e");
			user.setRole(role);
			session.persist(user);
		}

		session.flush();
		session.close();

	}

	private boolean isItService(Method method) {
		return method.getAnnotation(GET.class) != null || method.getAnnotation(PUT.class) != null || method.getAnnotation(POST.class) != null || method.getAnnotation(DELETE.class) != null || method.getAnnotation(OPTIONS.class) != null;
	}
}