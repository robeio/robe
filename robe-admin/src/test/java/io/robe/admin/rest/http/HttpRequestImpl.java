package io.robe.admin.rest.http;

import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kamilbukum on 28/09/16.
 */
public class HttpRequestImpl implements HttpRequest {

    private String cookie;

    public HttpRequestImpl() {

    }

    public HttpRequestImpl(String cookie) {
        this.cookie = cookie;
    }
    /**
     *
     */
    public final OkHttpClient client = new OkHttpClient();

    /**
     *
     * @param status
     * @param message
     * @param bodyString
     * @param headerMap
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> resultToResponse(int status, String message, String bodyString, Map<String, String> headerMap) throws IOException {
        io.robe.admin.rest.Response<String> response = new io.robe.admin.rest.Response<>();
        response.setStatus(status);
        response.setMessage(message);
        if(response.getStatus() == 200) {
            response.setData(bodyString);
        }
        this.cookie = headerMap.get("Set-Cookie");
        response.setHeaderMap(headerMap);
        return response;
    }

    /**
     *
     * @param headers
     * @return
     */
    public Map<String, String> fromHeadersToMap(Headers headers){
        Map<String, String> map = new LinkedHashMap<>();
        if(headers != null && headers.size() > 0) {
            for(int i = 0 ; i< headers.size(); i++) {
                map.put(headers.name(i), headers.value(i));
            }
        }
        return map;
    }

    /**
     *
     * @param builder
     * @param headerMap
     */
    public void setHeaderToRequest(Request.Builder builder, Map<String, String> headerMap){
        if(headerMap != null && headerMap.size() > 0) {
            for(Map.Entry<String,String> entry: headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     *
     * @param url
     * @param queryMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> get(String url, String id, Map<String, String> queryMap, Map<String, String> headerMap) throws IOException {
        String queryString = null;
        if(queryMap != null) {
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<String,String> entry: queryMap.entrySet()) {
                builder.append(entry.getKey()).append("=").append(entry.getValue());
            }
            queryString = builder.toString();
        }
        return get(url, id, queryString, headerMap);
    }

    /**
     *
     * @param url
     * @param queryString
     * @param headerMap
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> get(String url, String id, String queryString, Map<String, String> headerMap) throws IOException {
        // add id to url
        url = getUrl(url, id);

        // add query params to url
        if (queryString != null) {
            queryString = queryString.trim();
            if(!queryString.equals("")) {
                url = url + "?" + queryString;
            }
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);
        return request(requestBuilder);
    }

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(ContentType.JSON.getMediaType(), json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body);
        return request(requestBuilder);
    }

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> put(String url, String id, String json) throws IOException {
        RequestBody body = RequestBody.create(ContentType.JSON.getMediaType(), json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(getUrl(url, id))
                .put(body);
        return request(requestBuilder);
    }

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> delete(String url, String id, String json) throws IOException {
        RequestBody body = RequestBody.create(ContentType.JSON.getMediaType(), json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(getUrl(url, id))
                .delete(body);
        return request(requestBuilder);
    }

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> patch(String url, String id, String json) throws IOException {
        RequestBody body = RequestBody.create(ContentType.JSON.getMediaType(), json);
        Request.Builder requestBuilder = new Request.Builder()
                .url(getUrl(url, id))
                .patch(body);
        return request(requestBuilder);

    }

    /**
     *
     * @param url
     * @param path
     * @return
     */
    static String getUrl(String url, String path) {
        if(path != null) {
            path.trim();
            if(!path.equals("")) {
                url = url.endsWith("/") ? url + path : url + "/" + path;
            }
        }
        return url;
    }

    /**
     *
     * @param builder
     * @return
     * @throws IOException
     */
    public io.robe.admin.rest.Response<String> request(Request.Builder builder) throws IOException {
        okhttp3.Response response = null;
        if(cookie != null) {
            builder.addHeader("Cookie", cookie);
        }
        response = new OkHttpClient().newCall(builder.build()).execute();
        String message = response.message();
        Map<String, String> headerMap = fromHeadersToMap(response.headers());
        String bodyString = response.body().string();
        int status = response.code();

        if(!response.isSuccessful()) {

            throw new IOException("Unexpected code " + response);
        }
        return resultToResponse(status, message, bodyString, headerMap);
    }

    /**
     * auth-token=544B3756303664453153557555534E2F6164696C56325747554D3061737A58486E5837697972746638337533374D4644694E73646C424C5A61484B4E6D7073426D5754796A4438446A747A77324D57522B59652F58677A7636394A4B5362302F6B6F38646733466E58536F6A3550595A314A6A756C3732765A4A6C72745675372B70694F4152643962454245616630434E71364B675671647957704C755965674B4B312B344C685645432B39466B4B784A384C2B46394A437547663066576A6C;path=/;domain=127.0.0.1;;max-age=1000
     * @param cookie
     * @return
     */
    public static Map<String, String> parseResponseCookie(String cookie) {
        String[] params = cookie.split(";");
        Map<String, String> cookieMap = new HashMap<>();
        for (String param : params) {
            String[] entry = param.split("=");
            if (entry.length > 1 && entry[0] != null && entry[1] != null) {
                cookieMap.put(entry[0], entry[1]);
            }
        }
        return cookieMap;
    }

}
