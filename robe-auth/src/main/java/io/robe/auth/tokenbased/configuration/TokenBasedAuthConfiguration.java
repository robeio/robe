package io.robe.auth.tokenbased.configuration;

public class TokenBasedAuthConfiguration {

    private String tokenKey;

    private String domain;

    private String path = "/";

    private int maxage = 0;

    private boolean secure = false;


    public String getTokenKey() {
        return tokenKey;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public int getMaxage() {
        return maxage;
    }

    public boolean isSecure() {
        return secure;
    }
}
