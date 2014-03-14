package io.robe.auth.esapi;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.Authenticator;
import io.robe.auth.IsToken;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * An {@link com.yammer.dropwizard.auth.Auth} injectable that obtains an injectable value given the HttpContext.
 * Authenticate and Authorize controls are executed by this class on every inject request.
 *
 * @param <T> The type of the injectable.
 */
public class ESAPIAuthInjectable<T extends IsToken> extends AbstractHttpContextInjectable<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ESAPIAuthInjectable.class);

	private final Authenticator<String, T> authenticator;

    /**
	 * {@inheritDoc}
	 *
	 * @param authenticator The authenticator which will be used with Authenticate controls.
	 */
	protected ESAPIAuthInjectable(Authenticator<String, T> authenticator) {
		this.authenticator = authenticator;
        String path = "";
        if ( path != null ) {
            ESAPI.securityConfiguration().setResourceDirectory( path );
        }
	}

	/**
	 * This method gets the context and does all necessary controls.
	 * Returns injectable or throws {@link javax.ws.rs.WebApplicationException} with response type {@link javax.ws.rs.core.Response.Status} UNAUTHORIZED
	 *
	 * @param c The http context of the inject request.
	 * @return Returns the desired injectable.
	 */
	@Override
	public T getValue(HttpContext c) {

        HttpServletRequest request = (HttpServletRequest) c.getRequest();
        HttpServletResponse response = (HttpServletResponse) c.getResponse();
        ESAPI.httpUtilities().setCurrentHTTP(request, response);

        try {
            // figure out who the current user is
            try {
                ESAPI.authenticator().login(request, response);
            } catch( org.owasp.esapi.errors.AuthenticationException e ) {
                throw new WebApplicationException(e,Response.Status.UNAUTHORIZED);

            }

            // check access to this URL
            if ( !ESAPI.accessController().isAuthorizedForURL(request.getRequestURI()) ) {
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }

            // check for CSRF attacks
            // ESAPI.httpUtilities().checkCSRFToken();

            // forward this request on to the web application

            // set up response with content type
//            ESAPI.httpUtilities().setContentType( response );

            // set no-cache headers on every response
            // only do this if the entire site should not be cached
            // otherwise you should do this strategically in your controller or actions
//            ESAPI.httpUtilities().setNoCacheHeaders( response );

        } catch (Exception e) {
            LOGGER.error("Error in ESAPI security filter: " + e.getMessage(), e );
        } finally {
            // VERY IMPORTANT
            // clear out the ThreadLocal variables in the authenticator
            // some containers could possibly reuse this thread without clearing the User
            ESAPI.clearCurrent();
        }

//        //TODO: take token key  form properties.
//		Cookie tokenList = c.getRequest().getCookies().get("auth-token");
//		if (tokenList == null || tokenList.getValue().length() == 0) {
//			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
//		}
//
//		String token = tokenList.getValue();
//		if (token == null || token.length() == 0) {
//			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
//		}
//		//Validate old token for auth token
//		try {
//			Optional<T> result = authenticator.authenticate(token);
//			if (!result.isPresent())
//				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
//
//			if (!isAuthorized(result.get(), ((WebApplicationContext) c).getMatchedTemplates(), c.getRequest().getMethod()))
//				throw new WebApplicationException(Response.Status.FORBIDDEN);
//
//			return result.get();
//		} catch (IllegalArgumentException e) {
//			LOGGER.debug("BasicPair decoding credentials", e);
//			throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
//		} catch (AuthenticationException e) {
//			LOGGER.warn("BasicPair authenticating credentials", e);
//			throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
//		}
        return null;
	}


	/**
	 * Merges all path patterns and and creates a single string value which will be equal with service methods path
     * annotation value and HTTP method type. Generated string will be used for permission checks.
	 *
	 * @param token      for checking permission list
	 * @param matchedTemplates matched templates of context. They will be merged with reverse order
	 * @param method           HTTP Method of the request. Will be merged with
	 * @return true if user is Authorized.
	 */
	private boolean isAuthorized(IsToken token, List<UriTemplate> matchedTemplates, String method) {
		StringBuilder path = new StringBuilder();
        // Merge all path templates and generate a path.
		for (UriTemplate template : matchedTemplates)
			path.insert(0, template.getTemplate());
		path.append(":").append(method);

        //Look at user permissions to see if the service is permitted.
		return token.getPermissions().contains(path.toString());
	}

}