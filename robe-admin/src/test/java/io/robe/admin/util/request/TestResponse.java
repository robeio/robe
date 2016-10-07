package io.robe.admin.util.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

/**
 * Created by adem on 06/10/2016.
 */
public class TestResponse {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TestRequest testRequest;
    private int status;
    private String message;
    private String data;
    private Map<String, String> headers;
    private Map<String, String> cookies;

    public TestRequest getTestRequest() {
        return testRequest;
    }

    private void setTestRequest(TestRequest testRequest) {
        this.testRequest = testRequest;
    }

    public int getStatus() {
        return status;
    }

    private void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    private void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    private void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getCookie(String name) {
        return cookies.get(name);
    }

    public static TestResponse fromResponse(TestRequest testRequest, Response response) throws IOException {
        TestResponse testResponse = new TestResponse();
        Map<String, String> headers = new HashMap<>();
        Set<String> headerNames = response.headers().names();
        Iterator<String> headerNamesIterator = headerNames.iterator();
        while(headerNamesIterator.hasNext()) {
            String headerName = headerNamesIterator.next();
            headers.put(headerName, response.header(headerName));
        }
        testResponse.setHeaders(headers);
        testResponse.setCookies(mapCookies(response.header("Set-Cookie")));
        testResponse.setData(response.body().string());
        testResponse.setStatus(response.code());
        testResponse.setTestRequest(testRequest);
        return testResponse;
    }

    public String asIs() {
        return this.data;
    }

    public <T> T get(Class<T> resultType) throws IOException {
        return OBJECT_MAPPER.readValue(this.data, resultType);
    }

    public <T> List<T> list(Class<T> resultType) throws IOException {
        return OBJECT_MAPPER.readValue(this.data, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, resultType));
    }

    private static Map<String, String> mapCookies(String cookieHeader) {
        if(cookieHeader == null) {
            return new HashMap<>();
        }
        String[] cookies = cookieHeader.split(";");
        Map<String, String> cookieMap = new HashMap<>();
        for(String cookie : cookies) {
            String[] cookieEntry = cookie.split("=");
            if(cookieEntry.length > 1)
                cookieMap.put(cookieEntry[0], cookieEntry[1]);
        }
        return cookieMap;
    }

}
