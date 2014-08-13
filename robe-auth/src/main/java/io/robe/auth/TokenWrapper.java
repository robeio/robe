package io.robe.auth;

import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public class TokenWrapper<T extends IsToken> {

    private static Class<? extends IsToken> type;
    private static int maxage = 0;

    private TokenWrapper() {
    }

    public static <I extends IsToken> void initialize(Class<I> clazz, TokenBasedAuthConfiguration configuration) {
        type = clazz;
        Method method = null;
        try {
            method = type.getMethod("initialize", TokenBasedAuthConfiguration.class);
            Object o = method.invoke(null, configuration);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static IsToken createToken(String token) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return type.getConstructor(String.class).newInstance(token);
    }

    /**
     * Creates an access token with the given {@link io.robe.auth.Credentials}
     *
     * @param username   Username to create token
     * @param attributes Extra attributes to include in token. (role,lang,...)
     * @return {@link io.robe.auth.IsToken} instance with n second expiration.
     * @throws java.lang.IllegalAccessException,java.lang.InstantiationException
     */
    public static IsToken createToken(String username, Map<String, String> attributes) throws IllegalAccessException, InstantiationException {
        IsToken token = type.newInstance();
        token.setUserAccountName(username);

        token.setExpiration(maxage);
        if (attributes != null && attributes.isEmpty()) {
            token.addAttributes(attributes);
        }
        return token;
    }

    public static void setMaxage(int maxage) {
        TokenWrapper.maxage = maxage;
    }
}
