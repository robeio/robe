package io.robe.auth.token.jersey;

import io.robe.auth.token.BasicToken;
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
    private static String cookieSentence = "";


    /**
     * Creates a valid token cookie template with the given configuration.
     *
     * @param configuration
     */
    public TokenBasedAuthResponseFilter(TokenBasedAuthConfiguration configuration) {
        this.tokenKey = configuration.getTokenKey();
        String domain = configuration.getDomain();
        String path = configuration.getPath();
        if (configuration.getMaxage() > 0l)
            cookieSentence += "; Max-Age=" + configuration.getMaxage();//+ "; Expires={expireDate}";
        if (domain != null && !domain.equals(""))
            cookieSentence += "; Domain=" + domain + ";";
        if (path != null && !path.equals(""))
            cookieSentence += "; Path=" + path;
        if (configuration.isSecure()) {
            cookieSentence += "; Secure;";
        }
        cookieSentence += "; HttpOnly";
    }

    /**
     * Combines the token and cookie sentence
     *
     * @param token
     * @return
     */
    public static String getTokenSentence(BasicToken token) throws Exception {
        if (token == null)
            return tokenKey + "=" + cookieSentence;
        String sentence = tokenKey + "=" + token.getTokenString() + cookieSentence;
        //TODO: Learn how to calculate expire according to the browser time.
//        sentence = sentence.replace("{expireDate}", token.getExpirationDate().toGMTString());
        return sentence;
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
                BasicToken token = new BasicToken(authToken);
                if (token.isExpired()) {
                    LOGGER.debug("ExpireDate : " + token.getExpirationDate().toString());
                    LOGGER.debug("Now: " + DateTime.now().toDate().toString());
                    responseContext.getHeaders().putSingle("Set-Cookie", getTokenSentence(null));
                    responseContext.setStatusInfo(Response.Status.UNAUTHORIZED);
                    responseContext.setEntity("Token expired. Please login again.");
                    LOGGER.info("Token expired. Please login again.");
                } else {
                    token.setExpiration(token.getMaxAge());
                    if (!"authentication/logout".equals(requestContext.getUriInfo().getPath())) {
                        String cookie = getTokenSentence(token);
                        responseContext.getHeaders().putSingle("Set-Cookie", cookie);
                    }
                }

            } catch (Exception e) {
                LOGGER.error("Token re-creation failed", e.getMessage());
                responseContext.setStatusInfo(Response.Status.UNAUTHORIZED);

            }
        }
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
