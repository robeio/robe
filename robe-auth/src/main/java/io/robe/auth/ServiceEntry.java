package io.robe.auth;

public interface ServiceEntry {
    public enum Method {
        GET,
        PUT,
        POST,
        DELETE,
        OPTIONS;
    }

    String getPath();

    Method getMethod();
}
