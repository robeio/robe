package io.robe.admin;


import io.dropwizard.testing.junit.DropwizardAppRule;
import io.robe.admin.util.request.Authenticator;
import org.junit.BeforeClass;
import org.junit.ClassRule;

/**
 * Created by kamilbukum on 22/09/16.
 */
public class RobeAdminTest {

    static {
        System.setProperty("env", "TEST");
    }

    @ClassRule
    public static final DropwizardAppRule<RobeConfiguration> RULE = new DropwizardAppRule(RobeApplication.class, "robe.yml");

    @BeforeClass
    public static void login() throws Exception {
        Authenticator.login("admin@robe.io", "123123");
    }

}