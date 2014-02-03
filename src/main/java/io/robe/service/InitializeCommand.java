package io.robe.service;

import com.google.common.base.Optional;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.EnvironmentCommand;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.HibernateBundle;
import io.robe.hibernate.dao.RoleDao;
import io.robe.hibernate.dao.ServiceDao;
import io.robe.hibernate.dao.UserDao;
import io.robe.hibernate.entity.Role;
import io.robe.hibernate.entity.User;
import net.sourceforge.argparse4j.inf.Namespace;
import org.hibernate.SessionFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.Set;


public class InitializeCommand<T extends RobeServiceConfiguration> extends EnvironmentCommand<T> {

    private HibernateBundle hibernateBundle;
    ServiceDao serviceDao;
    UserDao userDao;
    RoleDao roleDao;

    public InitializeCommand(Service<T> service,HibernateBundle hibernateBundle) {
        super(service, "initialize", "Runs Hibernate and initialize required columns");
        this.hibernateBundle = hibernateBundle;

    }


    @Override
    @UnitOfWork
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {

        final Logger logger = LoggerFactory.getLogger(InitializeCommand.class);
        SessionFactory sessionFactory =hibernateBundle.getSessionFactory();

        serviceDao = new ServiceDao(sessionFactory);
        execute();


    }



    @UnitOfWork
    @POST
    private void execute() {
        Reflections reflections = new Reflections("io",this.getClass().getClassLoader());
        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Path.class);
        for(Class service : services){
            String parentPath = "/"+((Path)service.getAnnotation(Path.class)).value();
            for(Method method : service.getMethods()){
                if(method.getAnnotation(Path.class) != null) {
                    String httpMethod = method.getAnnotation(GET.class) != null ? "GET" :
                            method.getAnnotation(POST.class) != null ? "POST":
                                    method.getAnnotation(PUT.class) != null ? "PUT":
                                            method.getAnnotation(DELETE.class) != null ? "DELETE":
                                                    method.getAnnotation(OPTIONS.class) != null ? "OPTIONS":"";
                    String path = parentPath + "/" + method.getAnnotation(Path.class).value() ;
                    path = path.replaceAll("//","/");
                    Optional<io.robe.hibernate.entity.Service> optional = serviceDao.findByPathAndMethod(path, io.robe.hibernate.entity.Service.Method.valueOf(httpMethod));
                    if(!optional.isPresent()){
                        io.robe.hibernate.entity.Service service1 = new io.robe.hibernate.entity.Service();
                        service1.setPath(path);
                        service1.setMethod(io.robe.hibernate.entity.Service.Method.valueOf(httpMethod));
                        serviceDao.create(service1);
                    }
                }
            }
        }
        Optional<Role> roleOptional = roleDao.findByName("Admin");
        if (!roleOptional.isPresent()){
            Role role = new Role();
            role.setCode("admin");
            role.setName("Admin");
            roleDao.create(role);
        }
        Optional<User> userOptional = userDao.findByEmail("admin@robe.io");
        if (!userOptional.isPresent()){
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
}
