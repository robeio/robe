package io.robe.auth.tokenbased.injectable;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.server.impl.application.WebApplicationContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import io.dropwizard.auth.Authenticator;
import io.robe.auth.Token;
import io.robe.auth.TokenFactory;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.List;


/**
 * An {@link io.dropwizard.auth.Auth} injectable that obtains an injectable value given the HttpContext.
 * Authenticate and Authorize controls are executed by this class on every inject request.
 *
 * @param <T> The type of the injectable.
 */
public class TokenBasedAuthInjectable<T extends Token> extends AbstractHttpContextInjectable<T> {

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
     * @param context The http context of the inject request.
     * @return Returns the desired injectable.
     */
    @Override
    public T getValue(HttpContext context) {

        Cookie tokenCookie = context.getRequest().getCookies().get(tokenKey);

        if (tokenCookie == null ) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else if (nullOrEmpty(tokenCookie.getValue())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            try {
                if(!isRealOwnerOfToken(context, tokenCookie)){
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
                Optional<T> result = authenticator.authenticate(tokenCookie.getValue());

                if (!result.isPresent()) {
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                } else if (!isAuthorized(result.get(), ((WebApplicationContext) context).getMatchedTemplates(), context.getRequest().getMethod())) {
                    throw new WebApplicationException(Response.Status.FORBIDDEN);
                } else {
                    return result.get();
                }
            } catch (Exception e) {
                LOGGER.debug("BasicPair decoding credentials", e);
                throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
            }
        }
    }

    private boolean isRealOwnerOfToken(HttpContext c, Cookie tokenCookie) throws Exception {
        Token token = TokenFactory.getInstance().createToken(tokenCookie.getValue());
        String hash = generateAttributesHash(c.getRequest());
        return hash.equals(token.getAttributesHash());

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
    private boolean isAuthorized(Token token, List<UriTemplate> matchedTemplates, String method) {
        StringBuilder path = new StringBuilder();
        // Merge all path templates and generate a path.
        for (UriTemplate template : matchedTemplates) {
            path.insert(0, template.getTemplate());
        }
        path.append(":").append(method);

        //Look at user permissions to see if the service is permitted.
        return token.getPermissions().contains(path.toString());
    }


    public String generateAttributesHash(HttpRequestContext request) {
        StringBuilder attr = new StringBuilder();
        attr.append(request.getHeaderValue("User-Agent"));
        attr.append(request.getRequestUri().getHost());
        return Hashing.sha256().hashString(attr.toString(), StandardCharsets.UTF_8).toString();
    }

}