package io.robe.admin;

import com.google.common.hash.Hashing;
import io.robe.admin.rest.Response;
import io.robe.admin.rest.RobeRestClient;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kamilbukum on 27/09/16.
 */
public class RobeApplicationTest extends RobeApplication<RobeConfiguration> {


    public static  Response<Map<String, String>> login(String username, String password) throws Exception {
        Map<String, String> stringMap = new LinkedHashMap<>();
        RobeRestClient<Map<String, String>, String> entityClient = new RobeRestClient(stringMap.getClass(),"authentication/login");

        Map<String, String> credentials = new LinkedHashMap<>();

        password = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        credentials.put("username", username);
        credentials.put("password", password);
        Response<Map<String, String>> response = entityClient.create(credentials);
        return response;
    }
}
