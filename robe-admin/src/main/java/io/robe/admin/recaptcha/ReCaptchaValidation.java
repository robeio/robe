package io.robe.admin.recaptcha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

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
public class ReCaptchaValidation {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String CHARSET_NAME = "UTF-8";
    private static ReCaptchaValidation instance = null;
    private String verifyUrl;
    private String secret;


    public ReCaptchaValidation(ReCaptchaConfiguration configuration) {
        Preconditions.checkNotNull(configuration.getVerifyUrl(), "re captcha verifyUrl cannot be null or empty. Check your yml file");
        Preconditions.checkNotNull(configuration.getSecret(), "re captcha secret cannot be null or empty. Check your yml file");

        this.verifyUrl = configuration.getVerifyUrl();
        this.secret = configuration.getSecret();
        instance = this;
    }

    public static ReCaptchaValidation getInstance() {
        Preconditions.checkNotNull(instance, "re captcha configuration cannot be null or empty. Check your yml file");
        return instance;
    }

    public ReCaptchaResponseBody validate(String verify) throws IOException {

        URL url = new URL(verifyUrl);

        Map<String, Object> params = new HashMap<>();
        params.put("secret", secret);
        params.put("response", verify);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), CHARSET_NAME));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), CHARSET_NAME));
        }

        byte[] postDataBytes = postData.toString().getBytes(CHARSET_NAME);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET_NAME));

        StringBuilder responseString = new StringBuilder();
        for (int c; (c = in.read()) >= 0; )
            responseString.append((char) c);
        String response = responseString.toString();

        return OBJECT_MAPPER.readValue(response, ReCaptchaResponseBody.class);
    }
}
