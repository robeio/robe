package io.robe.admin.util.request;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * Created by adem on 06/10/2016.
 */
public class HttpClient implements HttpRequest {

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new AuthenticationInterceptor()).build();

    private static final HttpClient INSTANCE = new HttpClient();

    private HttpClient() {
    }

    public static HttpClient getClient() {
        return INSTANCE;
    }

    @Override
    public TestResponse post(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        if(testRequest.hasBody()) {
            requestBuilder.post(RequestBody.create(MediaType.parse(testRequest.getContentType()), testRequest.getEntity()));
        }
        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        return TestResponse.fromResponse(testRequest, response);
    }

    @Override
    public TestResponse get(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        return TestResponse.fromResponse(testRequest, response);
    }

    @Override
    public TestResponse put(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        if(testRequest.hasBody()) {
            requestBuilder.put(RequestBody.create(MediaType.parse(testRequest.getContentType()), testRequest.getEntity()));
        }
        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        return TestResponse.fromResponse(testRequest, response);
    }

    @Override
    public TestResponse delete(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        if(testRequest.hasBody()) {
            requestBuilder.delete(RequestBody.create(MediaType.parse(testRequest.getContentType()), testRequest.getEntity()));
        }
        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        return TestResponse.fromResponse(testRequest, response);
    }

    @Override
    public TestResponse patch(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        if(testRequest.hasBody()) {
            requestBuilder.patch(RequestBody.create(MediaType.parse(testRequest.getContentType()), testRequest.getEntity()));
        }
        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        return TestResponse.fromResponse(testRequest, response);
    }

    private Request.Builder buildRequest(TestRequest testRequest) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(testRequest.getRequestUrl());

        if(testRequest.hasCookie())
            for(Map.Entry<String, String> cookie : testRequest.getCookies().entrySet())
                requestBuilder.addHeader("Cookie", cookie.getKey() + "=" + cookie.getValue());
        if(testRequest.hasHeader())
            for(Map.Entry<String, String> header : testRequest.getHeaders().entrySet())
                requestBuilder.addHeader(header.getKey(), header.getValue());

        requestBuilder.header("Content-Type", testRequest.getContentType());

        return requestBuilder;
    }

}
