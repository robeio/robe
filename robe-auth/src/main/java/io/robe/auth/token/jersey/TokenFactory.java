package io.robe.auth.token.jersey;

import com.google.common.hash.Hashing;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.robe.auth.Credentials;
import io.robe.auth.token.BasicToken;
import org.glassfish.jersey.server.internal.inject.AbstractContainerRequestValueFactory;
import org.glassfish.jersey.uri.UriTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class TokenFactory<T extends BasicToken> extends AbstractContainerRequestValueFactory<Credentials> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenFactory.class);

    public static Authenticator<String, BasicToken> authenticator;

    public static String tokenKey;

    private boolean required = true;

    public TokenFactory(boolean required) {
        this.required = required;
    }

    public TokenFactory() {
    }

    public static Credentials createEmptyCredentials() {

        return new Credentials() {
            @Override
            public String getUserId() {
                return null;
            }

            @Override
            public String getUsername() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public Credentials provide() {
        Cookie tokenCookie = getContainerRequest().getCookies().get(tokenKey);

        if (isRequired()) {


            if (tokenCookie == null) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            } else if (nullOrEmpty(tokenCookie.getValue())) {
                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
            } else {
                try {
                    if (!isRealOwnerOfToken(tokenCookie)) {
                        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
                    }
                    Optional<BasicToken> result = authenticator.authenticate(tokenCookie.getValue());

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
                } catch (Exception e) {
                    LOGGER.error("Authentication Exception  (Is Real ownwer of token) ", e);
                    throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
                }
            }
        } else {
            if (tokenCookie == null) {
                return createEmptyCredentials();
            } else {
                try {
                    Optional<BasicToken> result = authenticator.authenticate(tokenCookie.getValue());
                    if (result.isPresent()) {
                        return result.get();
                    } else {
                        return createEmptyCredentials();
                    }
                } catch (AuthenticationException e) {
                    // TODO ignore this error
                    return createEmptyCredentials();
                }

            }
        }
    }

    private boolean isRealOwnerOfToken(Cookie tokenCookie) throws Exception {
        LOGGER.debug("HttpContext : " + this.getContainerRequest().getPath(true) + " Cookie : " + tokenCookie);
        BasicToken token = new BasicToken(tokenCookie.getValue());
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
     * @param token            for checking permission list
     * @param matchedTemplates matched templates of context. They will be merged with reverse order
     * @param method           HTTP Method of the request. Will be merged with
     * @return true if user is Authorized.
     */
    private boolean isAuthorized(BasicToken token, List<UriTemplate> matchedTemplates, String method) {
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