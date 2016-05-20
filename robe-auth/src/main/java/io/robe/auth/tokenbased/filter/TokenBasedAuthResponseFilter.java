package io.robe.auth.tokenbased.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import io.robe.auth.tokenbased.Token;
import io.robe.auth.tokenbased.TokenFactory;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;


/**
 * The response filter for creating or refreshing AuthTokens. Refreshing controlled by created token.
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
        if (path != null && !path.equals(""))
            cookieSentence += ";path=" + path;
        if (domain != null && !domain.equals(""))
            cookieSentence += ";domain=" + domain + ";";
        if (configuration.getMaxage() > 0l) {
            cookieSentence += ";max-age=" + configuration.getMaxage();
        }
        if (configuration.isSecure()) {
            cookieSentence += "secure;";
        }
    }


    /**
     * Checks the expiration date of token.
     * Renews and puts at header of response.
     * @param request
     * @param response
     * @return
     */
    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
        String authToken = extractAuthTokenFromCookieList(request.getHeaderValue("Cookie"));
        if (authToken != null && authToken.length() != 0) {
            try {
                Token token = TokenFactory.getInstance().createToken(authToken);
                if (token.isExpired()) {
                    LOGGER.debug("ExpireDate : " + token.getExpirationDate().toString());
                    LOGGER.debug("Now: " + DateTime.now().toDate().toString());
                    response.getHttpHeaders().putSingle("Set-Cookie", getTokenSentence(""));
                    response.setStatusType(Response.Status.UNAUTHORIZED);
                    response.setEntity("Token expired. Pleas login again.");
                    LOGGER.info("Token expired. Pleas login again.");
                } else {
                    token.setExpiration(token.getMaxAge());
                    response.getHttpHeaders().putSingle("Set-Cookie", getTokenSentence(token.getTokenString()));
                }

            } catch (Exception e) {
                LOGGER.error("Token re-creation failed", e.getMessage());
                response.setStatusType(Response.Status.UNAUTHORIZED);

            }
        }

        return response;


    }

    /**
     * Combines the token and cookie sentence
     * @param authToken final cookie
     * @return
     */
    public static String getTokenSentence(String authToken) {
        return tokenKey + "=" + authToken + cookieSentence;
    }

    /**
     * Extracts the accesstoken from cookies
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
