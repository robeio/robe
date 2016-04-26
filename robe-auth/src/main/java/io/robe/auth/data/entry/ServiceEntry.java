package io.robe.auth.data.entry;

public interface ServiceEntry {
    public enum Method {
        GET,
        PUT,
        POST,
        PATCH,
        DELETE,
        OPTIONS;
    }
    public String getPath();

    public Method getMethod();
}
