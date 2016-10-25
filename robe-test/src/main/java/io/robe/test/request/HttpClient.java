package io.robe.test.request;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * Created by adem on 06/10/2016.
 */
public class HttpClient implements HttpRequest {

    private final OkHttpClient okHttpClient;

    private static HttpClient INSTANCE;

    private HttpClient(Interceptor interceptor) {
        okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    public static HttpClient getClient(Interceptor interceptor) {
        if(INSTANCE == null) {
            INSTANCE = new HttpClient(interceptor);
        }
        return INSTANCE;
    }

    public static HttpClient getClient() {
        if(INSTANCE == null) {
            INSTANCE = new HttpClient(new AuthenticationInterceptor());
        }
        return INSTANCE;
    }

    @Override
    public TestResponse post(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        String entity = testRequest.hasBody() ? testRequest.getEntity() : "";
        requestBuilder.post(RequestBody.create(MediaType.parse(testRequest.getContentType()), entity));
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
        String entity = testRequest.hasBody() ? testRequest.getEntity() : "";
        requestBuilder.put(RequestBody.create(MediaType.parse(testRequest.getContentType()), entity));
        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        return TestResponse.fromResponse(testRequest, response);
    }

    @Override
    public TestResponse delete(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        String entity = testRequest.hasBody() ? testRequest.getEntity() : "";
        requestBuilder.delete(RequestBody.create(MediaType.parse(testRequest.getContentType()), entity));
        Response response = okHttpClient.newCall(requestBuilder.build()).execute();
        return TestResponse.fromResponse(testRequest, response);
    }

    @Override
    public TestResponse patch(TestRequest testRequest) throws IOException {
        Request.Builder requestBuilder = buildRequest(testRequest);
        String entity = testRequest.hasBody() ? testRequest.getEntity() : "";
        requestBuilder.patch(RequestBody.create(MediaType.parse(testRequest.getContentType()), entity));
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
