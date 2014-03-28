package io.robe.auth.esapi;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
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

    /**
	 * {@inheritDoc}
	 *
	 */
	protected ESAPIAuthInjectable() {
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

            // figure out who the current user is
            try {
                ESAPI.authenticator().login(request, response);
            } catch( org.owasp.esapi.errors.AuthenticationException e ) {
                ESAPI.clearCurrent();
                throw new WebApplicationException(e,Response.Status.UNAUTHORIZED);
            }

            // check access to this URL
            if ( !ESAPI.accessController().isAuthorizedForURL(request.getRequestURI()) ) {
                ESAPI.clearCurrent();
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            }

            // forward this request on to the web application

            // set up response with content type
            ESAPI.httpUtilities().setContentType( response );


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