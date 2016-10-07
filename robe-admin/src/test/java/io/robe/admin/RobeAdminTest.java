package io.robe.admin;


import io.dropwizard.testing.ResourceHelpers;
import io.robe.admin.util.request.Authenticator;

/**
 * Created by kamilbukum on 22/09/16.
 */
public class RobeAdminTest {

    static {
        try {
            System.setProperty("env", "TEST");
            RobeApplication.main(new String[]{"server", ResourceHelpers.resourceFilePath("robe_test.yml")});
            String username = "admin@robe.io";
            String password = "123123";
            Authenticator.login(username, password);
        } catch (Exception e) {
            throw new RuntimeException("Application couldn't started ! Detail ", e);
        }
    }

}