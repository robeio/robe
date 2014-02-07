package io.robe.mail;

public class MailProfile {
    private String host;
    private int port;
    private boolean auth;
    private String username;
    private String password;
    private boolean tlsssl;

    public MailProfile() {
        super();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTlsssl() {
        return tlsssl;
    }

    public void setTlsssl(boolean tlsssl) {
        this.tlsssl = tlsssl;
    }
}
