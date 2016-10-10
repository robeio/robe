package io.robe.admin.resources;

import com.google.common.hash.Hashing;
import io.robe.admin.RobeAdminTest;
import io.robe.admin.hibernate.entity.User;
import io.robe.admin.util.junit.Order;
import io.robe.admin.util.junit.Roadrunner;
import io.robe.admin.util.request.Authenticator;
import io.robe.admin.util.request.HttpClient;
import io.robe.admin.util.request.TestRequest;
import io.robe.admin.util.request.TestResponse;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by adem on 07/10/2016.
 */
@RunWith(Roadrunner.class)
public class AuthenticationResourceTest extends RobeAdminTest {

    private final HttpClient client = HttpClient.getClient();
    private final TestRequest.Builder requestBuilder = new TestRequest.Builder("http://127.0.0.1:8080/robe/authentication");

    private final String tokenHeaderName = "auth-token";
    private static String TOKEN;

    private static final String USERNAME = "admin@robe.io";
    private static String PASSWORD = Hashing.sha256().hashString("123123", StandardCharsets.UTF_8).toString();

    @Before
    public void checkIfAuthTokenPresent() throws Exception {
        if (TOKEN == null) {
            login();
        }
    }

    @Test
    @Order
    public void login() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", USERNAME);
        credentials.put("password", PASSWORD);
        TestRequest request = requestBuilder.entity(credentials).endpoint("login").build();
        TestResponse response = client.post(request);
        assertEquals(response.getStatus(), 200);
        assertNotNull(response.getCookie(tokenHeaderName));
        TOKEN = response.getCookie(tokenHeaderName);
    }

    @Test
    @Order(order = 2)
    public void getProfile() throws Exception {
        TestRequest request = requestBuilder.endpoint("profile").header(tokenHeaderName, "").build();
        TestResponse response = client.get(request);
        assertEquals(response.getStatus(), 200);
        User user = response.get(User.class);
        assertNotNull(user);
        assertEquals(user.getEmail(), USERNAME);
        assertEquals(user.getPassword(), PASSWORD);
    }

    @Test
    @Order(order = 3)
    public void changePassword() throws Exception {
        String newPassword = Hashing.sha256().hashString("321321", StandardCharsets.UTF_8).toString();
        Map<String, String> passwords = new HashMap<>();
        passwords.put("password", PASSWORD);
        passwords.put("newPassword", newPassword);
        passwords.put("newPasswordRepeat", newPassword);
        TestRequest request = requestBuilder.endpoint("password").entity(passwords).header(tokenHeaderName, TOKEN).build();
        TestResponse response = client.post(request);
        assertEquals(response.getStatus(), 200);
        PASSWORD = newPassword;
    }

    @Test
    @Order(order = 4)
    public void logout() throws Exception {
        TestRequest request = requestBuilder.endpoint("logout").header(tokenHeaderName, TOKEN).build();
        TestResponse response = client.post(request);
        assertEquals(response.getStatus(), 200);
        assertEquals(response.getCookie("auth-token"), "");
    }

    @AfterClass
    public static void loginAgain() throws Exception {
        Authenticator.login(USERNAME, "321321");
    }

}
