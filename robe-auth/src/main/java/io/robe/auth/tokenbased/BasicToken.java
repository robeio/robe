package io.robe.auth.tokenbased;

import com.codahale.metrics.annotation.Timed;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;
import io.robe.auth.Token;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BasicToken implements Token {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicToken.class);

    private static final PooledPBEStringEncryptor ENCRYPTOR = new PooledPBEStringEncryptor();
    private static final String SEPARATOR = "--";
    private static int defaultMaxAge;

    private static Cache<String, Set<String>> cache;


    private String username;
    private DateTime expireAt;
    private String attributesHash;
    private String tokenString;
    private int maxAge;


    /**
     * Initialize method for Token generation configurations and ENCRYPTOR configure
     *
     * @param configuration
     */
    public static void configure(TokenBasedAuthConfiguration configuration) {
        ENCRYPTOR.setPoolSize(configuration.getPoolSize());          // This would be a good value for a 4-core system
        if (configuration.getServerPassword().equals("auto")) {
            ENCRYPTOR.setPassword(UUID.randomUUID().toString());
        } else {
            ENCRYPTOR.setPassword(configuration.getServerPassword());
        }
        ENCRYPTOR.setAlgorithm(configuration.getAlgorithm());
        ENCRYPTOR.initialize();
        BasicToken.defaultMaxAge = configuration.getMaxage();

        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(defaultMaxAge, TimeUnit.SECONDS)
                .expireAfterWrite(defaultMaxAge, TimeUnit.SECONDS)
                .build();
    }

    public BasicToken(String username, DateTime expireAt, String attributesHash) {
        this.username = username;
        this.expireAt = expireAt;
        this.attributesHash = attributesHash;
        this.maxAge = defaultMaxAge;
    }

    public BasicToken(String username, DateTime expireAt, Map<String, String> attributes) {
        this.username = username;
        this.expireAt = expireAt;
        this.maxAge = defaultMaxAge;
        generateAttributesHash(attributes);
    }

    public BasicToken(Token token) {
        this.username = token.getUsername();
        this.expireAt = new DateTime(token.getExpirationDate());
        this.attributesHash = token.getAttributesHash();
        this.maxAge = token.getMaxAge();
    }

    public BasicToken(String token) throws Exception {
        try {
            token = new String(Hex.decodeHex(token.toCharArray()));
            String[] parts = ENCRYPTOR.decrypt(token).split(SEPARATOR);
            this.username = parts[0];
            this.expireAt = new DateTime(Long.valueOf(parts[1]));
            this.attributesHash = parts[2];
        } catch (DecoderException e) {
            LOGGER.error("Cant decode token: " + token, e);
            throw e;
        }

    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isExpired() {
        return !expireAt.isAfterNow();
    }

    @Override
    public void setExpiration(int durationInSeconds) {
        expireAt = DateTime.now().plusSeconds(durationInSeconds);
        resetTokenString();
    }

    @Override
    public Date getExpirationDate() {
        return expireAt.toDate();
    }


    @Override
    public void updateAttributeHash(Map<String, String> map) {
        generateAttributesHash(map);
        resetTokenString();
    }

    @Override
    public String getAttributesHash() {
        return attributesHash;
    }

    private void generateAttributesHash(Map<String, String> attributes) {
        StringBuilder attr = new StringBuilder();
        attr.append(attributes.get("userAgent"));
        attr.append(attributes.get("remoteAddr"));
        attributesHash = Hashing.sha256().hashString(attr.toString(), StandardCharsets.UTF_8).toString();
        resetTokenString();
    }

    public String getTokenString() throws Exception {
        return tokenString == null ? generateTokenString() : tokenString;
    }

    /**
     * Generates a tokenString with a new expiration date and assigns it.
     *
     * @return new tokenString
     * @throws Exception
     */
    @Timed
    private String generateTokenString() throws Exception {
        //Renew age
        //Stringify token data
        StringBuilder dataString = new StringBuilder();
        dataString.append(getUsername())
                .append(SEPARATOR)
                .append(getExpirationDate().getTime())
                .append(SEPARATOR)
                .append(attributesHash);

        // Encrypt token data string
        String newTokenString = ENCRYPTOR.encrypt(dataString.toString());
        newTokenString = Hex.encodeHexString(newTokenString.getBytes());
        tokenString = newTokenString;
        return newTokenString;
    }

    public int getMaxAge() {
        return maxAge < 1 ? defaultMaxAge:maxAge;
    }

    @Override
    public void setPermissions(Set<String> permissions) {
        cache.put(getUsername(), permissions);
    }

    @Override
    public Set<String> getPermissions() {
        return cache.getIfPresent(getUsername());
    }

    public void setMaxAge(int maxAge) {
        if (maxAge < 1) maxAge = defaultMaxAge;
        this.maxAge = maxAge;
    }


    private void resetTokenString() {
        tokenString = null;
    }
}
