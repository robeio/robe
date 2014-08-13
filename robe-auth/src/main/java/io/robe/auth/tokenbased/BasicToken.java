package io.robe.auth.tokenbased;

import io.robe.auth.Credentials;
import io.robe.auth.IsToken;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BasicToken implements IsToken<BasicToken>, Credentials {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicToken.class);

    private static final PooledPBEStringEncryptor ENCRYPTOR = new PooledPBEStringEncryptor();
    private static final String SEPARATOR = "--";
    private String username;
    private DateTime expireAt;
    private int maxage;
    private Set<String> permissions;

    public static void initialize(TokenBasedAuthConfiguration configuration) {
        ENCRYPTOR.setPoolSize(configuration.getPoolSize());          // This would be a good value for a 4-core system
        if (configuration.getServerPassword().equals("auto")) {
            ENCRYPTOR.setPassword(UUID.randomUUID().toString());
        } else {
            ENCRYPTOR.setPassword(configuration.getServerPassword());
        }
        ENCRYPTOR.setAlgorithm(configuration.getAlgorithm());
        ENCRYPTOR.initialize();
    }


    public BasicToken(String token) throws DecoderException {
        try {
            token = new String(Hex.decodeHex(token.toCharArray()));
        } catch (DecoderException e) {
            LOGGER.error("Cant decode token: " + token, e);
            throw e;
        }
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
        String token = ENCRYPTOR.encrypt(getUsername() + SEPARATOR + getExpirationDate().getTime());
        token = Hex.encodeHexString(token.getBytes());
        return token;
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
