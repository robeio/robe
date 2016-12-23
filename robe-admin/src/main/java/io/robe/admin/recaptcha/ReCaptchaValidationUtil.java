package io.robe.admin.recaptcha;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hasanmumin on 23/12/2016.
 */
public class ReCaptchaValidationUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static ReCaptchaResponseBody validate(String verify) throws IOException {

        URL url = new URL("https://www.google.com/recaptcha/api/siteverify");

        Map<String, Object> params = new HashMap<>();
        params.put("secret", "6LckQA8TAAAAAAuwC602KfqYuxOCSiXBqFS3m6OO");
        params.put("response", verify);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder responseString = new StringBuilder();
        for (int c; (c = in.read()) >= 0; )
            responseString.append((char) c);
        String response = responseString.toString();

        return OBJECT_MAPPER.readValue(response, ReCaptchaResponseBody.class);
    }
}
