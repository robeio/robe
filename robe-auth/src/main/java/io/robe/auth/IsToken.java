package io.robe.auth;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface IsToken<T> {
    String getUserAccountName();
    void setUserAccountName(String username) throws Exception;
    boolean isExpired();
    void setExpiration(int durationInSeconds);
    void setExpiration(Date expireDate);
    long getExpiration();
    Date getExpirationDate();
    void setAttribute(String key, String value) throws Exception;
    void addAttributes(Map<String,String> map) throws Exception;
    String getAttribute(String key);
    Map<String,String> getAttributes();
    void clearAttributes();
    String getToken() throws Exception;

    void setPermissions(Set<String> permissions);

    Set<String> getPermissions();
}
