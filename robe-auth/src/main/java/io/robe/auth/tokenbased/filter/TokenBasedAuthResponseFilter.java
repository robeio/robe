package io.robe.auth.tokenbased.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import io.robe.auth.IsToken;
import io.robe.auth.TokenWrapper;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The response filter for creating or refreshing AuthTokens. Refreshing controlled by created token.
 */
public class TokenBasedAuthResponseFilter implements ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenBasedAuthResponseFilter.class);
    private static String tokenKey;
    private static String cookieSentence = ";path=%s;domain%s";


    TokenBasedAuthResponseFilter(TokenBasedAuthConfiguration configuration) {
        this.tokenKey = configuration.getTokenKey();
        String domain = configuration.getDomain();
        String path = configuration.getPath();

        cookieSentence = ";path=" + path + ";" + "domain=" + domain + ";";
        if (configuration.getMaxage() > 0l) {
            cookieSentence = ";max-age=" + configuration.getMaxage() + cookieSentence;
        }
        if (configuration.isSecure()) {
            cookieSentence = cookieSentence + "secure;";
        }
    }


    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        String authToken = extractAuthTokenFromCookieList(request.getHeaderValue("Cookie"));
        if (authToken != null && authToken.length() != 0) {
            try {
                IsToken cryptoToken = TokenWrapper.createToken(authToken);
                if (cryptoToken.isExpired()) {
                    authToken = cryptoToken.getToken();
                }
            } catch (Exception e) {
                LOGGER.error("Token re-creation failed", e);
            }
            response.getHttpHeaders().putSingle("Set-Cookie", getTokenSentence(authToken));
        }
        return response;

    }

    public static String getTokenSentence(String authToken) {
        return tokenKey + "=" + authToken + cookieSentence;
    }

    private String extractAuthTokenFromCookieList(String cookieList) {
        if (cookieList == null || cookieList.length() == 0) {
            return null;
        }
        String[] cookies = cookieList.split(";");
        for (String cookie : cookies) {
            if (cookie.trim().startsWith(tokenKey)) {
                return cookie.trim().substring(tokenKey.length() + 1);
            }
        }
        return null;
    }
}
