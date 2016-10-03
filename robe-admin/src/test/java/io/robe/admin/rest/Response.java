package io.robe.admin.rest;

import java.util.Map;

/**
 * Created by kamilbukum on 27/09/16.
 */
public class Response<T> {
    private int status;
    private String message;
    private T data;
    private Map<String, String> headerMap;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
