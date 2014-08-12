package io.robe.auth.tokenbased;

import io.robe.auth.Credentials;
import io.robe.auth.IsToken;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.joda.time.DateTime;

import java.util.*;

public class BasicToken implements IsToken<BasicToken>, Credentials {
    private static final String SEPARATOR = "--";
    private String username;
    private DateTime expireAt;
    private int maxage;
    private Set<String> permissions;
    private static final PooledPBEStringEncryptor ENCRYPTOR = new PooledPBEStringEncryptor();

    static {
        ENCRYPTOR.setPoolSize(4);          // This would be a good value for a 4-core system
        ENCRYPTOR.setPassword(UUID.randomUUID().toString());
        ENCRYPTOR.setAlgorithm("PBEWithMD5AndTripleDES");
        ENCRYPTOR.initialize();
    }


    public BasicToken(String token) {
        String[] dec = ENCRYPTOR.decrypt(token).split(SEPARATOR);
        username = dec[0];
        expireAt = new DateTime(Long.valueOf(dec[1]));

    }

    public BasicToken() {

    }


    @Override
    public String getUsername() {
        return username == null ? "GUEST" : username;
    }

    @Override
    public String getUserAccountName() {
        return getUsername();
    }

    @Override
    public void setUserAccountName(String username) {
        this.username = username;
    }

    @Override
    public boolean isExpired() {
        return !expireAt.isAfterNow();
    }

    @Override
    public void setExpiration(int durationInSeconds) {
        maxage = durationInSeconds;
        expireAt = DateTime.now().plus(durationInSeconds * 1000);
    }

    @Override
    public void setExpiration(Date expireDate) {
        if (Calendar.getInstance().getTimeInMillis() < expireDate.getTime()) {
            expireAt = new DateTime(expireDate);
        }
    }

    @Override
    public long getExpiration() {
        return maxage;
    }

    @Override
    public Date getExpirationDate() {
        return expireAt.toDate();
    }


    @Override
    public String getToken() throws Exception {
        setExpiration(maxage);
        return ENCRYPTOR.encrypt(getUsername() + SEPARATOR + getExpirationDate().getTime());
    }

    @Override
    public void setPermissions(Set<String> permissions) {
        this.permissions = Collections.unmodifiableSet(permissions);
    }

    @Override
    public Set<String> getPermissions() {
        return permissions;
    }

    @Override
    public void setAttribute(String key, String value) {

    }

    @Override
    public void addAttributes(Map<String, String> map) {

    }

    @Override
    public String getAttribute(String key) {
        return null;
    }

    @Override
    public Map<String, String> getAttributes() {
        return null;
    }

    @Override
    public void clearAttributes() {

    }
}
