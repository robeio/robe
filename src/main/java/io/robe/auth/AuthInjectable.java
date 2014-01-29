package io.robe.auth;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.uri.UriTemplate;
import com.sun.jersey.server.impl.application.WebApplicationContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * An {@link com.yammer.dropwizard.auth.Auth} injectable that obtains an injectable value given the HttpContext.
 * Authenticate and Authorize controls are executed by this class on every inject request.
 *
 * @param <T> The type of the injectable.
 */
public class AuthInjectable<T extends Credentials> extends AbstractHttpContextInjectable<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthInjectable.class);

	private final Authenticator<String, T> authenticator;

	/**
	 * {@inheritDoc}
	 *
	 * @param authenticator The authenticator which will be used with Authenticate controls.
	 */
	protected AuthInjectable(Authenticator<String, T> authenticator) {
		this.authenticator = authenticator;
	}

	/**
	 * This method gets the context and does all necessary controls.
	 * Returns injectable or throws {@link javax.ws.rs.WebApplicationException} with responce type {@link Response.Status} UNAUTHORIZED
	 *
	 * @param c The http context of the inject request.
	 * @return Returns the desired injectable.
	 */
	@Override
	public T getValue(HttpContext c) {

		Cookie tokenList = c.getRequest().getCookies().get("auth-token");
		if (tokenList == null || tokenList.getValue().length() == 0) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}

		String token = tokenList.getValue();
		if (token == null || token.length() == 0) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		//Validate old token for auth token
		try {
			Optional<T> result = authenticator.authenticate(token);
			if (!result.isPresent())
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);

			if (!isAuthorized(result.get(), ((WebApplicationContext) c).getMatchedTemplates(), c.getRequest().getMethod()))
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);

			return result.get();
		} catch (IllegalArgumentException e) {
			LOGGER.debug("BasicPair decoding credentials", e);
			throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
		} catch (AuthenticationException e) {
			LOGGER.warn("BasicPair authenticating credentials", e);
			throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
		}
	}


	/**
	 * Matches path pattern  and merges it for comparing with path annotations value.
	 *
	 * @param credentials      for checking permission list
	 * @param matchedTemplates matched templates of context. They will be merged with reverse order
	 * @param method           HTTP Method of the request. Will be merged with
	 * @return true if user is Authorized.
	 */
	private boolean isAuthorized(Credentials credentials, List<UriTemplate> matchedTemplates, String method) {
		StringBuilder path = new StringBuilder();
		for (UriTemplate template : matchedTemplates)
			path.insert(0, template.getTemplate());
		path.append(":").append(method);

		return credentials.getPermissions().contains(path.toString());
	}

}