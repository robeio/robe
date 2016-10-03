package io.robe.admin.rest.http;

import okhttp3.MediaType;
import okhttp3.Request;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by kamilbukum on 27/09/16.
 */
public interface HttpRequest {


    /**
     *
     * @param url
     * @param id
     * @param queryMap
     * @param headerMap
     * @return
     * @throws IOException
     */
    io.robe.admin.rest.Response<String> get(String url, String id, Map<String, String> queryMap, Map<String, String> headerMap) throws IOException;

    /**
     *
     * @param url
     * @param queryString
     * @param headerMap
     * @return
     * @throws IOException
     */
    io.robe.admin.rest.Response<String> get(String url, String id, String queryString, Map<String, String> headerMap) throws IOException;

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    io.robe.admin.rest.Response<String> post(String url, String json) throws IOException;

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    io.robe.admin.rest.Response<String> put(String url, String id, String json) throws IOException;

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    io.robe.admin.rest.Response<String> delete(String url, String id, String json) throws IOException;

    /**
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    io.robe.admin.rest.Response<String> patch(String url, String id, String json) throws IOException;


    /**
     *
     * @param builder
     * @return
     * @throws IOException
     */
    io.robe.admin.rest.Response<String> request(Request.Builder builder) throws IOException;


    enum ContentType {
        IMAGE(MediaType.parse("image/png")),
        JSON(MediaType.parse("application/json; charset=utf-8"));

        private MediaType mediaType;
        ContentType(MediaType mediaType) {
            this.mediaType = mediaType;
        }

        public MediaType getMediaType() {
            return mediaType;
        }
    }
}
