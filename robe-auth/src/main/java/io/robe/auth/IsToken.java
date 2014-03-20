package io.robe.auth;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface IsToken<T> {
    String getUserAccountName();
    void setUserAccountName(String username);
    boolean isExpired();
    void setExpiration(int durationInSeconds);
    void setExpiration(Date expireDate);
    long getExpiration();
    Date getExpirationDate();
    void setAttribute(String key, String value);
    void addAttributes(Map<String,String> map);
    String getAttribute(String key);
    Map<String,String> getAttributes();
    void clearAttributes();
    String getToken() throws Exception;

    void setPermissions(Set<String> permissions);

    Set<String> getPermissions();
}
