package io.robe.auth.data.entry;

public interface ServiceEntry {
    public enum Method {
        GET,
        PUT,
        POST,
        DELETE,
        OPTIONS;
    }
    public String getPath();

    public Method getMethod();
}
