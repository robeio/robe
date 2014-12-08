package io.robe.auth;

import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;


public class TokenFactory<I extends Token> {

    private static TokenFactory instance = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFactory.class);
    private static Class<? extends Token> tokenClass;

    private static Constructor constructor2;
    private static Constructor constructor3;
    private static Constructor constructor4;

    private TokenFactory() {

    }

    public  static <I extends Token> TokenFactory getInstance() {
        if (instance == null)
            instance = new TokenFactory<I>();
        {
        }
        return instance;
    }

    /**
     * Configure the factory and token.  Cache the constructors of token class for future use
     *
     * @param tokenClass
     * @param configuration
     * @param <I>
     */
    public static <I extends Token> void configure(Class<I> tokenClass, TokenBasedAuthConfiguration configuration) throws Exception {
        TokenFactory.tokenClass = tokenClass;
        Method configureMethod = null;

        LOGGER.debug("Configuring token class : " + tokenClass.getName());
        configureMethod = tokenClass.getMethod("configure", TokenBasedAuthConfiguration.class);
        configureMethod.invoke(null, configuration);

        LOGGER.debug("Caching constructors");
        constructor2 = tokenClass.getConstructor(String.class, DateTime.class, Map.class);
        if (constructor2 == null) {
            throw new RuntimeException("Missing constructor implementation");
        }
        constructor3 = tokenClass.getConstructor(Token.class);
        if (constructor3 == null) {
            throw new RuntimeException("Missing constructor implementation");
        }
        constructor4 = tokenClass.getConstructor(String.class);
        if (constructor4 == null) {
            throw new RuntimeException("Missing constructor implementation");
        }
        TokenFactory.<I>getInstance();

    }


    //constructor 2

    /**
     * Creates an access token with the given {@link io.robe.auth.Credentials}
     *
     * @param username   Username to create token
     * @param attributes Extra attributes to include in token. (role,lang,...)
     * @return {@link Token} instance with n second expiration.
     * @throws java.lang.IllegalAccessException,java.lang.InstantiationException
     */
    public Token createToken(String username, DateTime expireAt, Map<String, String> attributes) throws Exception {
        return (I) constructor2.newInstance(username, expireAt, attributes);
    }

    //constructor 3
    public Token createToken(Token token) throws Exception {
        return (I) constructor3.newInstance(token);
    }

    //constructor 4
    public Token createToken(String tokenString) throws Exception {
        return (I) constructor4.newInstance(tokenString);
    }


}
