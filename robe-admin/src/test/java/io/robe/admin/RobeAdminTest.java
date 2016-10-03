package io.robe.admin;


import io.robe.admin.rest.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.util.Map;

/**
 * Created by kamilbukum on 22/09/16.
 */

@Ignore
public class RobeAdminTest {

    private static String cookie;

    public static String getCookie() {
        return cookie;
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.setProperty("env", "TEST");
        RobeApplicationTest.main(new String [] {"server", "robe_test.yml"});
        String username = "admin@robe.io";
        String password = "123123";
        Response<Map<String, String>> responseMap = RobeApplicationTest.login(username, password);
        if(responseMap.getStatus() == 200) {
            for(Map.Entry<String, String> entry: responseMap.getHeaderMap().entrySet()) {
                System.out.println("---------- " + entry.getKey() + " -------------");
                System.out.println(entry.getValue());
                System.out.println("-----------------------------------------------");
                if("Set-Cookie".equals(entry.getKey())) {
                    cookie = entry.getValue();
                }
            }
        } else {
            System.err.println("Login Failed ! Result : " + responseMap);
        }
    }
    @AfterClass
    public static void afterClass(){
        System.exit(1);
    }
}