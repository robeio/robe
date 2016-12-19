package io.robe.admin;


import io.dropwizard.testing.ResourceHelpers;
import io.robe.admin.cli.InitializeCommand;
import io.robe.admin.util.Authenticator;
import io.robe.hibernate.RobeHibernateBundle;

/**
 * Created by kamilbukum on 22/09/16.
 */
public class RobeAdminTest {

    static {
        try {
            System.setProperty("env", "TEST");
            RobeApplication application = new RobeApplication();
            application.run(new String[]{"server", ResourceHelpers.resourceFilePath("robe_test.yml")});

            InitializeCommand command = new InitializeCommand(application, RobeHibernateBundle.getInstance());
            command.execute(application.getConfiguration());

            String username = "admin@robe.io";
            String password = "123123";
            Authenticator.login(username, password);
        } catch (Exception e) {
            throw new RuntimeException("Application couldn't started ! Detail ", e);
        }
    }

}