package io.robe.auth;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


public class TokenWrapper<T extends IsToken> {

    private static Class<? extends IsToken> type;

    public static <I extends  IsToken> void  initialize(Class<I> clazz) {
        type = clazz;
    }

    public static IsToken createToken(String token) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return type.getConstructor(String.class).newInstance(token);
    }

    /**
     * Creates an access token with the given {@link io.robe.auth.Credentials}
     *
     * @param username Username to create token
     * @param attributes Extra attributes to include in token. (role,lang,...)

     * @return {@link io.robe.auth.IsToken} instance with n second expiration.
     * @throws java.lang.IllegalAccessException,java.lang.InstantiationException
     */
    public static IsToken createToken(String username, Map<String, String> attributes) throws Exception {
        try {
        IsToken token = type.newInstance();

            token.setUserAccountName(username);
        //TODO: get expiration from properties.
        token.setExpiration(600);
        if(attributes != null && attributes.size()>0)
            token.addAttributes(attributes);
        return token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
