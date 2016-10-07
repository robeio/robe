package io.robe.admin.util.request;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by adem on 06/10/2016.
 */
public class Authenticator {

    public static void login(String username, String password) throws Exception {
        HttpClient client = HttpClient.getClient();
        Map<String, String> credentials = new LinkedHashMap<>();
        password = Hashing.sha256()
                            .hashString(password, StandardCharsets.UTF_8)
                            .toString();
        credentials.put("username", username);
        credentials.put("password", password);
        client.post(new TestRequest.Builder("http://127.0.0.1:8080/robe").
                                        entity(credentials).
                                        endpoint("authentication/login").build());
    }

}
