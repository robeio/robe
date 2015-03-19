package io.robe.auth.tokenbased;

import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;


/**
 * A singleton factory class for configuring and creating token implementations.
 * It wraps and caches 2 main constructors of token class.
 *
 * @param <I>
 */
public class TokenFactory<I extends Token> {

    private static TokenFactory instance = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFactory.class);
    private static Class<? extends Token> tokenClass;

    private static Constructor constructorByParameters;
    private static Constructor constructorByTokenString;

    private static ThreadLocal<Token> currentLoginToken = new ThreadLocal<Token>();

    private TokenFactory() {

    }

    static void setCurrentLoginToken(Token token) {
        currentLoginToken.set(token);
    }

    public static Token getCurrentLoginToken() {
        return currentLoginToken.get();
    }

    /**
     * Creates a single instance for factory.
     *
     * @param <I> Token class
     * @return instance
     */
    public static <I extends Token> TokenFactory getInstance() {
        if (instance == null)
            instance = new TokenFactory<I>();
        {
        }
        return instance;
    }

    /**
     * Configure the factory and token.  Cache the constructors of token class for future use.
     *
     * @param tokenClass
     * @param configuration
     * @param <I>           Token Class
     */
    public static <I extends Token> void configure(Class<I> tokenClass, TokenBasedAuthConfiguration configuration) throws Exception {
        TokenFactory.tokenClass = tokenClass;
        Method configureMethod = null;

        LOGGER.info("Configuring token class : " + tokenClass.getName());
        configureMethod = tokenClass.getMethod("configure", TokenBasedAuthConfiguration.class);
        if (configureMethod != null)
            configureMethod.invoke(null, configuration);
        else
            LOGGER.info("Configuring token class : " + tokenClass.getName() + " : Failed No such method");

        LOGGER.debug("Caching constructors");
        constructorByParameters = tokenClass.getConstructor(String.class, String.class, DateTime.class, Map.class);
        if (constructorByParameters == null) {
            LOGGER.error("Constructor (String username, DateTime expireAt, Map<String, String> attributes): Missing constructor implementation.");
            throw new RuntimeException("Missing constructor implementation");
        }
        LOGGER.debug("Constructor (String username, DateTime expireAt, Map<String, String> attributes): Loaded");


        constructorByTokenString = tokenClass.getConstructor(String.class);
        if (constructorByTokenString == null) {
            LOGGER.error("Constructor (String tokenString): Missing constructor implementation.");
            throw new RuntimeException("Missing constructor implementation");
        }
        LOGGER.debug("Constructor (String tokenString): Loaded.");

        TokenFactory.<I>getInstance();

    }

    /**
     * Creates an access token with the given parameters.
     *
     * @param username   Username
     * @param expireAt   expiration time of token
     * @param attributes extra attributes to customize token
     * @return
     * @throws Exception
     */
    public Token createToken(String userId, String username, DateTime expireAt, Map<String, String> attributes) throws Exception {
        return (I) constructorByParameters.newInstance(userId, username, expireAt, attributes);
    }

    /**
     * Creates an access token with the given tokenString.
     *
     * @param tokenString to parse
     * @return
     * @throws Exception
     */
    public Token createToken(String tokenString) throws Exception {
        return (I) constructorByTokenString.newInstance(tokenString);
    }


}
