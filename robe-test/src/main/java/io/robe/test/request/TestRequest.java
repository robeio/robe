package io.robe.test.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adem on 06/10/2016.
 */
public class TestRequest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String baseUrl;
    private String endpoint;
    private String entity;
    private String contentType;
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();
    private Map<String, String> headers = new HashMap<>();

    private TestRequest(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.endpoint = builder.endpoint;
        this.entity = builder.entity;
        this.contentType = builder.contentType;
        this.queryParams = builder.queryParams;
        this.cookies = builder.cookies;
        this.headers = builder.headers;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getEntity() {
        return entity;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public boolean hasCookie() {
        return !cookies.isEmpty();
    }

    public boolean hasHeader() {
        return !headers.isEmpty();
    }

    public boolean hasBody() {
        return entity != null;
    }

    public String getRequestUrl() {
        if (this.endpoint == null) {
            this.endpoint = "";
        }
        String requestUrl = this.baseUrl.endsWith("/") ? this.baseUrl + this.endpoint : this.baseUrl + "/" + this.endpoint;
        if (!this.queryParams.isEmpty()) {
            List<String> queryParts = new ArrayList<>();
            requestUrl += "?";
            for (Map.Entry<String, String> queryParam : this.getQueryParams().entrySet()) {
                queryParts.add(queryParam.getKey() + "=" + queryParam.getValue());
            }
            requestUrl += String.join("&", queryParts);
        }
        return requestUrl;
    }

    public static class Builder {

        private String baseUrl;
        private String endpoint;
        private String entity;
        private String contentType;
        private Map<String, String> queryParams = new HashMap<>();
        private Map<String, String> cookies = new HashMap<>();
        private Map<String, String> headers = new HashMap<>();

        public Builder(String baseUrl) {
            this(baseUrl, "application/json");
        }

        public Builder(String baseUrl, String contentType) {
            this.baseUrl = baseUrl;
            this.contentType = contentType;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder query(String name, String value) {
            this.queryParams.put(name, value);
            return this;
        }

        public Builder search(Object searchModel) throws IllegalArgumentException {
            if (searchModel == null) {
                return this;
            }
            Map<String, Object> search = OBJECT_MAPPER.convertValue(searchModel, new TypeReference<Map>(){});
            if (search.get("q") != null) {
                this.query("_q", search.get("q").toString());
            }
            if (search.get("fields") != null) {
                this.query("_fields", String.join(",", (List) search.get("fields")));
            }
            if (search.get("limit") != null) {
                this.query("_limit", search.get("limit").toString());
            }
            if (search.get("offset") != null) {
                this.query("_offset", search.get("offset").toString());
            }
            if (search.get("sort") != null) {
                this.query("_sort", String.join(",", (List) search.get("sort")));
            }
            if (search.get("filter") != null) {
                this.query("_filter", search.get("filter").toString());
            }
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder entity(String entity) {
            this.entity = entity;
            return this;
        }

        public Builder entity(Object entity) throws JsonProcessingException {
            this.entity = OBJECT_MAPPER.writeValueAsString(entity);
            return this;
        }

        public Builder header(String name, String value) {
            this.headers.put(name, value);
            return this;
        }

        public Builder cookie(String name, String value) {
            this.cookies.put(name, value);
            return this;
        }

        public TestRequest build() {
            return new TestRequest(this);
        }

    }

}
