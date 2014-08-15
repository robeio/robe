package io.robe.auth.tokenbased.injectable;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.server.impl.application.WebApplicationContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.robe.auth.IsToken;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * An {@link io.dropwizard.auth.Auth} injectable that obtains an injectable value given the HttpContext.
 * Authenticate and Authorize controls are executed by this class on every inject request.
 *
 * @param <T> The type of the injectable.
 */
public class TokenBasedAuthInjectable<T extends IsToken> extends AbstractHttpContextInjectable<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenBasedAuthInjectable.class);

    private final Authenticator<String, T> authenticator;

    private final String tokenKey;

    /**
     * {@inheritDoc}
     *
     * @param authenticator The authenticator which will be used with Authenticate controls.
     */
    protected TokenBasedAuthInjectable(Authenticator<String, T> authenticator, TokenBasedAuthConfiguration configuration) {
        this.authenticator = authenticator;
        this.tokenKey = configuration.getTokenKey();
    }

    /**
     * This method gets the context and does all necessary controls.
     * Returns injectable or throws {@link javax.ws.rs.WebApplicationException} with response type {@link Response.Status} UNAUTHORIZED
     *
     * @param c The http context of the inject request.
     * @return Returns the desired injectable.
     */
    @Override
    public T getValue(HttpContext c) {

        Cookie tokenList = c.getRequest().getCookies().get(tokenKey);
        if (tokenList == null || tokenList.getValue().length() == 0) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else if (nullOrEmpty(tokenList.getValue())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            //Validate old token for auth token
            try {
                Optional<T> result = authenticator.authenticate(tokenList.getValue());
                if (!result.isPresent()) {
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                } else if (!isAuthorized(result.get(), ((WebApplicationContext) c).getMatchedTemplates(), c.getRequest().getMethod())) {
                    throw new WebApplicationException(Response.Status.FORBIDDEN);
                } else {
                    return result.get();
                }
            } catch (IllegalArgumentException e) {
                LOGGER.debug("BasicPair decoding credentials", e);
                throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
            } catch (AuthenticationException e) {
                LOGGER.warn("BasicPair authenticating credentials", e);
                throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
            }
        }
    }

    private boolean nullOrEmpty(String token) {
        return token == null || token.length() == 0;
    }


    /**
     * Merges all path patterns and and creates a single string value which will be equal with service methods path
     * annotation value and HTTP method type. Generated string will be used for permission checks.
     *
     * @param token            for checking permission list
     * @param matchedTemplates matched templates of context. They will be merged with reverse order
     * @param method           HTTP Method of the request. Will be merged with
     * @return true if user is Authorized.
     */
    private boolean isAuthorized(IsToken token, List<UriTemplate> matchedTemplates, String method) {
        StringBuilder path = new StringBuilder();
        // Merge all path templates and generate a path.
        for (UriTemplate template : matchedTemplates) {
            path.insert(0, template.getTemplate());
        }
        path.append(":").append(method);

        //Look at user permissions to see if the service is permitted.
        return token.getPermissions().contains(path.toString());
    }

}