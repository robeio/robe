package io.robe.auth.token.jersey;

import io.robe.auth.token.Token;
import io.robe.auth.token.TokenManager;
import io.robe.auth.token.configuration.TokenBasedAuthConfiguration;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;


/**
 * The response jersey for creating or refreshing AuthTokens. Refreshing controlled by created token.
 */
public class TokenBasedAuthResponseFilter implements ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenBasedAuthResponseFilter.class);
    private static String tokenKey;
    private static String cookieSentence;


    /**
     * Creates a valid token cookie template with the given configuration.
     *
     * @param configuration
     */
    public TokenBasedAuthResponseFilter(TokenBasedAuthConfiguration configuration) {
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


    /**
     * Checks the expiration date of token.
     * Renews and puts at header of response.
     *
     * @param requestContext
     * @param responseContext
     * @return
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String authToken = extractAuthTokenFromCookieList(requestContext.getHeaders().getFirst("Cookie"));
        if (authToken != null && authToken.length() != 0) {
            try {
                Token token = TokenManager.getInstance().createToken(authToken);
                if (token.isExpired()) {
                    LOGGER.debug("ExpireDate : " + token.getExpirationDate().toString());
                    LOGGER.debug("Now: " + DateTime.now().toDate().toString());
                    responseContext.getHeaders().putSingle("Set-Cookie", getTokenSentence(""));
                    responseContext.setStatusInfo(Response.Status.UNAUTHORIZED);
                    responseContext.setEntity("Token expired. Please login again.");
                    LOGGER.info("Token expired. Please login again.");
                } else {
                    token.setExpiration(token.getMaxAge());
                    if (!"authentication/logout".equals(requestContext.getUriInfo().getPath()))
                        responseContext.getHeaders().putSingle("Set-Cookie", getTokenSentence(token.getTokenString()));
                }

            } catch (Exception e) {
                LOGGER.error("Token re-creation failed", e.getMessage());
                responseContext.setStatusInfo(Response.Status.UNAUTHORIZED);

            }
        }
    }

    /**
     * Combines the token and cookie sentence
     *
     * @param authToken final cookie
     * @return
     */
    public static String getTokenSentence(String authToken) {
        return tokenKey + "=" + authToken + cookieSentence;
    }

    /**
     * Extracts the accesstoken from cookies
     *
     * @param cookieList
     * @return
     */
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
