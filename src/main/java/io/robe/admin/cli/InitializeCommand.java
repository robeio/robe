package io.robe.admin.cli;

import com.google.inject.Inject;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.EnvironmentCommand;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.admin.RobeServiceConfiguration;
import io.robe.admin.hibernate.entity.Menu;
import io.robe.admin.hibernate.entity.Permission;
import io.robe.admin.hibernate.entity.Role;
import io.robe.admin.hibernate.entity.User;
import io.robe.hibernate.HibernateBundle;
import net.sourceforge.argparse4j.inf.Namespace;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;


public class InitializeCommand<T extends RobeServiceConfiguration> extends EnvironmentCommand<T> {

    @Inject
    private HibernateBundle hibernateBundle;


    public InitializeCommand(Service<T> service) {
        super(service, "initialize", "Runs Hibernate and initialize required columns");
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

            Role user = new Role();
            user.setCode("user");
            user.setName("User");
            session.persist(user);

            Role all = new Role();
            all.setCode("all");
            all.setName("All");
            all.setRoles(new HashSet<Role>());
            all.getRoles().add(user);
            all.getRoles().add(role);
            session.persist(all);
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
                    io.robe.admin.hibernate.entity.Service entity =
                            (io.robe.admin.hibernate.entity.Service) session.createCriteria(io.robe.admin.hibernate.entity.Service.class)
                                    .add(Restrictions.eq("path", path))
                                    .add(Restrictions.eq("method", io.robe.admin.hibernate.entity.Service.Method.valueOf(httpMethod)))
                                    .uniqueResult();
                    if (entity == null) {
                        entity = new io.robe.admin.hibernate.entity.Service();
                        entity.setPath(path);
                        entity.setMethod(io.robe.admin.hibernate.entity.Service.Method.valueOf(httpMethod));
                        session.persist(entity);
                        session.persist(createPermission(false, entity.getOid(), role));
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

        Language systemLanguageTR = new Language();
        systemLanguageTR.setCode(Language.Type.TR);
        systemLanguageTR.setName("Türkçe");
        session.persist(systemLanguageTR);
        Language systemLanguageEN = new Language();
        systemLanguageEN.setCode(Language.Type.EN);
        systemLanguageEN.setName("İngilizce");
        session.persist(systemLanguageEN);

        Menu root = new Menu();
        root.setCode("root");
        root.setItemOrder(1);
        root.setName("Menü");
        session.persist(root);
        session.persist(createPermission(true, root.getOid(), role));
        Menu manager = new Menu();
        manager.setCode("Manager");
        manager.setItemOrder(1);
        manager.setName("Yönetici");
        manager.setParentOid(root.getOid());
        session.persist(manager);
        session.persist(createPermission(true, manager.getOid(), role));

        Menu usermanagement = new Menu();
        usermanagement.setCode("UserManagement");
        usermanagement.setItemOrder(1);
        usermanagement.setName("Kullanıcı Yönetimi");
        usermanagement.setParentOid(manager.getOid());
        session.persist(usermanagement);
        session.persist(createPermission(true, usermanagement.getOid(), role));

        Menu rolemanagement = new Menu();
        rolemanagement.setCode("RoleManagement");
        rolemanagement.setItemOrder(1);
        rolemanagement.setName("Rol Yönetimi");
        rolemanagement.setParentOid(manager.getOid());
        session.persist(rolemanagement);
        session.persist(createPermission(true, rolemanagement.getOid(), role));

        Menu menumanagement = new Menu();
        menumanagement.setCode("MenuManagement");
        menumanagement.setItemOrder(1);
        menumanagement.setName("Menü Yönetimi");
        menumanagement.setParentOid(manager.getOid());
        session.persist(menumanagement);
        session.persist(createPermission(true, menumanagement.getOid(), role));

        Menu permissionManagement = new Menu();
        permissionManagement.setCode("PermissionManagement");
        permissionManagement.setItemOrder(1);
        permissionManagement.setName("İzin Atama");
        permissionManagement.setParentOid(manager.getOid());
        session.persist(permissionManagement);
        session.persist(createPermission(true, permissionManagement.getOid(), role));

        Menu dash = new Menu();
        dash.setCode("Dashboard");
        dash.setItemOrder(0);
        dash.setName("Dash");
        dash.setParentOid(manager.getOid());
        session.persist(dash);
        session.persist(createPermission(true, dash.getOid(), role));

        Menu mailTemplate = new Menu();
        mailTemplate.setCode("MailTemplateManagement");
        mailTemplate.setItemOrder(0);
        mailTemplate.setName("Mail Template Yönetimi");
        mailTemplate.setParentOid(manager.getOid());
        session.persist(mailTemplate);
        session.persist(createPermission(true, mailTemplate.getOid(), role));

        Menu quartzJob = new Menu();
        quartzJob.setCode("QuartzJobManagement");
        quartzJob.setItemOrder(0);
        quartzJob.setName("Quartz Job Manager");
        quartzJob.setParentOid(manager.getOid());
        session.persist(quartzJob);
        session.persist(createPermission(true, quartzJob.getOid(), role));

        session.flush();
        session.close();

    }

    private Permission createPermission(boolean b, String oid, Role role) {
        Permission permission = new Permission();
        permission.setpLevel((short) 7);
        permission.setType(b ? Permission.Type.MENU : Permission.Type.SERVICE);
        permission.setRestrictedItemOid(oid);
        permission.setRole(role);
        return permission;
    }

    private boolean isItService(Method method) {
        return method.getAnnotation(GET.class) != null || method.getAnnotation(PUT.class) != null || method.getAnnotation(POST.class) != null || method.getAnnotation(DELETE.class) != null || method.getAnnotation(OPTIONS.class) != null;
    }
}