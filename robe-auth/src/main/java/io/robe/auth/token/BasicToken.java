package io.robe.auth.token;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import io.robe.auth.Credentials;
import io.robe.auth.token.configuration.TokenBasedAuthConfiguration;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.joda.time.DateTime;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * A basic token implementation. Uses jasypt for encrypt & decrypt operations.
 * Takes all properties from configuration. Uses Guava for permission caching.
 * All cached permission entries will live with token.
 */
public class BasicToken implements Credentials {

    private static final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    private static final String SEPARATOR = "--";
    private static int defaultMaxAge;

    private static Cache<String, Set<String>> cache;

    private String userId;
    private String username;
    private DateTime expireAt;
    private String attributesHash;
    private String tokenString;
    private int maxAge;

    /**
     * Creates an access token with the given parameters.
     *
     * @param username   Username
     * @param expireAt   expiration time of token
     * @param attributes extra attributes to customize token
     * @return an instance
     */
    public BasicToken(String userId, String username, DateTime expireAt, Map<String, String> attributes) {
        this.userId = userId;
        this.username = username;
        this.expireAt = expireAt;
        this.maxAge = defaultMaxAge;
        generateAttributesHash(attributes);
    }

    /**
     * Creates an access token with the given tokenString.
     *
     * @param tokenString to parse
     * @return and instance
     * @throws Exception throws in case of failing opening token
     */
    public BasicToken(String tokenString) throws Exception {
        tokenString = tokenString.replaceAll("\"", "");
        tokenString = new String(BaseEncoding.base16().decode(tokenString));
        String[] parts = encryptor.decrypt(tokenString).split(SEPARATOR);
        this.userId = parts[0];
        this.username = parts[1];
        this.expireAt = new DateTime(Long.valueOf(parts[2]));
        this.attributesHash = parts[3];
    }

    /**
     * Configure method for Token generation configurations and encryptor configure
     *
     * @param configuration confiuration for auth bundle
     */
    public static void configure(TokenBasedAuthConfiguration configuration) {
        encryptor.setPoolSize(configuration.getPoolSize());          // This would be a good value for a 4-core system
        if (configuration.getServerPassword().equals("auto")) {
            encryptor.setPassword(UUID.randomUUID().toString());
        } else {
            encryptor.setPassword(configuration.getServerPassword());
        }
        encryptor.setAlgorithm(configuration.getAlgorithm());
        encryptor.initialize();
        BasicToken.defaultMaxAge = configuration.getMaxage();

        //Create cache for permissions.
        cache = CacheBuilder.newBuilder()
                .expireAfterAccess(defaultMaxAge, TimeUnit.SECONDS)
                .expireAfterWrite(defaultMaxAge, TimeUnit.SECONDS)
                .build();

    }

    public static void clearPermissionCache(String username) {
        cache.invalidate(username);
        cache.cleanUp();
    }

    public static void clearAllPermissionCache() {
        cache.invalidateAll();
        cache.cleanUp();
    }

    public static Set<String> getCurrentUsernames() {
        cache.cleanUp();
        return cache.asMap().keySet();
    }


    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }


    public boolean isExpired() {
        return !expireAt.isAfterNow();
    }


    public void setExpiration(int durationInSeconds) {
        expireAt = DateTime.now().plusSeconds(durationInSeconds);
        resetTokenString();
    }


    public Date getExpirationDate() {
        return expireAt.toDate();
    }

    public String getAttributesHash() {
        return attributesHash;
    }

    /**
     * Generates attribute has with 'userAgent', 'remoteAddr' keys.
     * Combines them and hashes with SHA256 and sets the variable.
     *
     * @param attributes
     */
    private void generateAttributesHash(Map<String, String> attributes) {
        StringBuilder attr = new StringBuilder();
        attr.append(attributes.get("userAgent"));
//        attr.append(attributes.get("remoteAddr")); TODO: add remote ip address after you find how to get remote IP from HttpContext
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
    private String generateTokenString() throws Exception {
        //Renew age
        //Stringify token data
        StringBuilder dataString = new StringBuilder();
        dataString
                .append(getUserId())
                .append(SEPARATOR)
                .append(getUsername())
                .append(SEPARATOR)
                .append(getExpirationDate().getTime())
                .append(SEPARATOR)
                .append(attributesHash);

        // Encrypt token data string
        String newTokenString = encryptor.encrypt(dataString.toString());
        newTokenString = BaseEncoding.base16().encode(newTokenString.getBytes());
        tokenString = newTokenString;
        return newTokenString;
    }

    public int getMaxAge() {
        return maxAge < 1 ? defaultMaxAge : maxAge;
    }

    public void setMaxAge(int maxAge) {
        if (maxAge < 1) maxAge = defaultMaxAge;
        this.maxAge = maxAge;
    }

    public Set<String> getPermissions() {
        return cache.getIfPresent(getUsername());
    }

    /**
     * Sets permissions to the cache with current username
     *
     * @param permissions permission list for the current user.
     */
    public void setPermissions(Set<String> permissions) {
        cache.put(getUsername(), permissions);
    }

    private void resetTokenString() {
        tokenString = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicToken)) return false;

        BasicToken that = (BasicToken) o;

        if (!userId.equals(that.userId)) return false;
        if (!username.equals(that.username)) return false;
        return attributesHash.equals(that.attributesHash);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + attributesHash.hashCode();
        return result;
    }

    public String getName() {
        return "BasicToken";
    }

    public static PooledPBEStringEncryptor getEncryptor() {
        return encryptor;
    }
}
