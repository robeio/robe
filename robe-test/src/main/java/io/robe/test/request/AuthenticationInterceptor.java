package io.robe.test.request;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by adem on 06/10/2016.
 */
public class AuthenticationInterceptor implements Interceptor {

    private final String requestHeaderName = "auth-token";
    private final String responseHeaderName = "Set-Cookie";

    private String token = "";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().addHeader("Cookie", requestHeaderName + "=" + token).build();
        Response response = chain.proceed(request);

        String responseHeader = response.header(responseHeaderName);
        if(responseHeader != null) {
            token = responseHeader.split("=")[1];
        }
        return response;
    }

}

