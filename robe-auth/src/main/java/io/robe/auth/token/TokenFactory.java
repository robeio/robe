package io.robe.auth.token;

import com.google.common.base.Optional;
import com.google.common.hash.Hashing;
import io.dropwizard.auth.Authenticator;
import io.robe.auth.Credentials;
import io.robe.auth.tokenbased.Token;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.uri.UriTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TokenFactory<T extends Token> extends AbstractContainerRequestValueFactory<Credentials> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFactory.class);

    public static Authenticator<String, Token> authenticator;

    public static String tokenKey;

    public TokenFactory() {
    }

    /**
     * {@inheritDoc}
     *
     * @param authenticator The authenticator which will be used with Authenticate controls.
     */
    protected TokenFactory(Authenticator<String, Token> authenticator, TokenBasedAuthConfiguration configuration) {
        this.authenticator = authenticator;
        this.tokenKey = configuration.getTokenKey();
    }


    @Override
    public Credentials provide() {
        Cookie tokenCookie = getContainerRequest().getCookies().get(tokenKey);

        if (tokenCookie == null) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else if (nullOrEmpty(tokenCookie.getValue())) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        } else {
            try {
                if (!isRealOwnerOfToken(tokenCookie)) {
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                }
                Optional<Token> result = authenticator.authenticate(tokenCookie.getValue());

                if (!result.isPresent()) {
                    throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                } else if (!isAuthorized(result.get(), getContainerRequest().getUriInfo().getMatchedTemplates(), getContainerRequest().getMethod())) {
                    throw new WebApplicationException(Response.Status.FORBIDDEN);
                } else {
                    return result.get();
                }
            } catch (io.dropwizard.auth.AuthenticationException e) {
                LOGGER.error("Authentication Exception  by Dropwizard", e);
                throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                LOGGER.error("Authentication Exception  (Is Real ownwer of token) ", e);
                throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
            }
        }
    }

    private boolean isRealOwnerOfToken(Cookie tokenCookie) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        LOGGER.debug("HttpContext : " + this.getContainerRequest().getPath(true) + " Cookie : " + tokenCookie);
        Token token = TokenManager.getInstance().createToken(tokenCookie.getValue());
        String hash = generateAttributesHash();
        return hash.equals(token.getAttributesHash());

    }

    private boolean nullOrEmpty(String token) {
        return token == null || token.length() == 0;
    }


    /**
     * Merges all path patterns and and creates a single string value which will be equal with service methods path
     * annotation value and HTTP method type. Generated string will be used for permission checks.
     *
     * @param token  for checking permission list
     * @param path   matched templates of context. They will be merged with reverse order
     * @param method HTTP Method of the request. Will be merged with
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


    public String generateAttributesHash() {
        StringBuilder attr = new StringBuilder();
        attr.append(this.getContainerRequest().getHeaderString("User-Agent"));
        return Hashing.sha256().hashString(attr.toString(), StandardCharsets.UTF_8).toString();
    }
}