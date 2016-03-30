package io.robe.admin.cli;

import com.google.common.hash.Hashing;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.setup.Environment;
import io.robe.admin.RobeConfiguration;
import io.robe.admin.hibernate.entity.*;
import io.robe.common.service.RobeService;
import io.robe.guice.GuiceConfiguration;
import io.robe.hibernate.RobeHibernateBundle;
import net.sourceforge.argparse4j.inf.Namespace;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.io.Console;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;


public class InitializeCommand<T extends RobeConfiguration> extends EnvironmentCommand<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializeCommand.class);
    protected static String IO_ROBE_ADMIN = "io/robe/admin";
    protected static String ADMIN = "Admin";
    protected RobeHibernateBundle hibernateBundle;

    public InitializeCommand(Application<T> service, RobeHibernateBundle hibernateBundle) {
        this(service, "initialize", "Runs Hibernate and initialize required columns", hibernateBundle);
    }

    public InitializeCommand(Application<T> service, String name, String description, RobeHibernateBundle hibernateBundle) {
        super(service, name, description);
        this.hibernateBundle = hibernateBundle;
    }


    @Override
    @UnitOfWork
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        LOGGER.info("Initialize Starting...");
        LOGGER.info("Starting to create initial data.");
        execute(configuration);
        System.exit(0);
    }


    @UnitOfWork
    public void execute(T configuration) {
        LOGGER.info("------------------------");
        LOGGER.info("------------------------");
        LOGGER.info("Please enter the admin password for the first :");

        Console console = System.console();

        String password;

        if (console != null) {
            // read password from server console
            password = new String(console.readPassword());
        } else {
            // read password from idea console
            Scanner scan = new Scanner(System.in);
            password = scan.nextLine();
        }

        password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        System.out.println("creating password " + password);
        LOGGER.info("------------------------");
        LOGGER.info("------------------------");

        final Session session = hibernateBundle.getSessionFactory().openSession();

        Role role = (Role) session.createCriteria(Role.class).add(Restrictions.eq("name", "Admin")).uniqueResult();
        LOGGER.info("Creating Roles.");
        if (role == null) {
            role = new Role();
            role.setCode(IO_ROBE_ADMIN);
            role.setName(ADMIN);
            session.persist(role);


            Role user = new Role();
            user.setCode("user");
            user.setName("User");
            session.persist(user);

            Role all = new Role();
            all.setCode("all");
            all.setName("All");
            session.persist(all);

            RoleGroup groupAllUser = new RoleGroup();
            groupAllUser.setRoleOid(user.getOid());
            groupAllUser.setGroupOid(all.getOid());
            session.persist(groupAllUser);

            RoleGroup groupAllAdmin = new RoleGroup();
            groupAllAdmin.setRoleOid(role.getOid());
            groupAllAdmin.setGroupOid(all.getOid());
            session.persist(groupAllAdmin);
        }

        GuiceConfiguration guiceConfiguration = configuration.getGuiceConfiguration();
        LOGGER.info("Scanning Services.Packages :" + Arrays.toString(guiceConfiguration.getScanPackages()));

        Reflections reflections = new Reflections(guiceConfiguration.getScanPackages(), this.getClass().getClassLoader());

        Set<Class<?>> services = reflections.getTypesAnnotatedWith(Path.class);
        for (Class<?> service : services) {

            String parentPath = "/" + service.getAnnotation(Path.class).value();
            for (Method method : service.getMethods()) {

                if (isItService(method)) {
                    String httpMethod = getHttpMethodType(method);

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
                        RobeService robeService = (RobeService) method.getAnnotation(RobeService.class);
                        if (robeService != null) {
                            entity.setDescription(robeService.description());
                            entity.setGroup(robeService.group());
                        }
                        session.persist(entity);
                        session.persist(createPermission(false, entity.getOid(), role));
                        LOGGER.info("Service data and permission created: " + entity.getPath() + "-" + entity.getMethod());
                    }

                }
            }
        }

        LOGGER.info("Creating admin user. U:admin@robe.io");
        User user = (User) session.createCriteria(User.class).add(Restrictions.eq("email", "admin@robe.io")).uniqueResult();
        if (user == null) {
            user = new User();
            user.setEmail("admin@robe.io");
            user.setActive(true);
            user.setName(IO_ROBE_ADMIN);
            user.setSurname(IO_ROBE_ADMIN);
            user.setPassword(password);
            user.setRoleOid(role.getOid());
            session.persist(user);

        }

        LOGGER.info("Creating languages : TR & EN");
        Language systemLanguageTR = new Language();
        systemLanguageTR.setCode(Language.Type.TR);
        systemLanguageTR.setName("Türkçe");
        session.persist(systemLanguageTR);
        Language systemLanguageEN = new Language();
        systemLanguageEN.setCode(Language.Type.EN);
        systemLanguageEN.setName("İngilizce");
        session.persist(systemLanguageEN);

        LOGGER.info("Createting Menu and permissions");
        Menu root = new Menu();
        root.setPath("root");
        root.setIndex(1);
        root.setText("Menü");
        session.persist(root);
        session.persist(createPermission(true, root.getOid(), role));

        Menu manager = new Menu();
        manager.setPath("Manager");
        manager.setIndex(1);
        manager.setText("Yönetici");
        manager.setParentOid(root.getOid());
        session.persist(manager);
        session.persist(createPermission(true, manager.getOid(), role));

        Menu userProfileManagement = new Menu();
        userProfileManagement.setPath("UserProfileManagement");
        userProfileManagement.setIndex(1);
        userProfileManagement.setText("Profil Yönetimi");
        userProfileManagement.setParentOid(manager.getOid());
        session.persist(userProfileManagement);
        session.persist(createPermission(true, userProfileManagement.getOid(), role));

        Menu usermanagement = new Menu();
        usermanagement.setPath("UserManagement");
        usermanagement.setIndex(1);
        usermanagement.setText("Kullanıcı Yönetimi");
        usermanagement.setParentOid(manager.getOid());
        session.persist(usermanagement);
        session.persist(createPermission(true, usermanagement.getOid(), role));

        Menu rolemanagement = new Menu();
        rolemanagement.setPath("RoleManagement");
        rolemanagement.setIndex(1);
        rolemanagement.setText("Rol Yönetimi");
        rolemanagement.setParentOid(manager.getOid());
        session.persist(rolemanagement);
        session.persist(createPermission(true, rolemanagement.getOid(), role));

        Menu menumanagement = new Menu();
        menumanagement.setPath("MenuManagement");
        menumanagement.setIndex(1);
        menumanagement.setText("Menü Yönetimi");
        menumanagement.setParentOid(manager.getOid());
        session.persist(menumanagement);
        session.persist(createPermission(true, menumanagement.getOid(), role));

        Menu permissionManagement = new Menu();
        permissionManagement.setPath("PermissionManagement");
        permissionManagement.setIndex(1);
        permissionManagement.setText("İzin Atama");
        permissionManagement.setParentOid(manager.getOid());
        session.persist(permissionManagement);
        session.persist(createPermission(true, permissionManagement.getOid(), role));

        Menu dash = new Menu();
        dash.setPath("Dashboard");
        dash.setIndex(0);
        dash.setText("Dash");
        dash.setParentOid(manager.getOid());
        session.persist(dash);
        session.persist(createPermission(true, dash.getOid(), role));

        Menu mailTemplate = new Menu();
        mailTemplate.setPath("MailTemplateManagement");
        mailTemplate.setIndex(0);
        mailTemplate.setText("Mail Template Yönetimi");
        mailTemplate.setParentOid(manager.getOid());
        session.persist(mailTemplate);
        session.persist(createPermission(true, mailTemplate.getOid(), role));

        Menu quartzJob = new Menu();
        quartzJob.setPath("QuartzJobManagement");
        quartzJob.setIndex(0);
        quartzJob.setText("Quartz Job Manager");
        quartzJob.setParentOid(manager.getOid());
        session.persist(quartzJob);
        session.persist(createPermission(true, quartzJob.getOid(), role));

        Menu systemParameter = new Menu();
        systemParameter.setPath("SystemParameter");
        systemParameter.setIndex(0);
        systemParameter.setText("System Parameter");
        systemParameter.setParentOid(manager.getOid());
        session.persist(systemParameter);
        session.persist(createPermission(true, systemParameter.getOid(), role));

        session.flush();
        session.close();

        LOGGER.info("Initialize finished. for robe");
    }

    protected Permission createPermission(boolean b, String oid, Role role) {
        Permission permission = new Permission();
        permission.setpLevel((short) 7);
        permission.setType(b ? Permission.Type.MENU : Permission.Type.SERVICE);
        permission.setRestrictedItemOid(oid);
        permission.setRoleOid(role.getOid());
        return permission;
    }

    protected boolean isItService(Method method) {
        return method.getAnnotation(GET.class) != null ||
                method.getAnnotation(PUT.class) != null ||
                method.getAnnotation(POST.class) != null ||
                method.getAnnotation(DELETE.class) != null ||
                method.getAnnotation(OPTIONS.class) != null;
    }

    protected String getHttpMethodType(Method method) {
        return method.getAnnotation(GET.class) != null ? "GET" :
                method.getAnnotation(POST.class) != null ? "POST" :
                        method.getAnnotation(PUT.class) != null ? "PUT" :
                                method.getAnnotation(DELETE.class) != null ? "DELETE" :
                                        method.getAnnotation(OPTIONS.class) != null ? "OPTIONS" : "";
    }
}