package io.robe.auth;

import com.google.common.base.Optional;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;

public class AuthInjectable<T> extends AbstractHttpContextInjectable {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthInjectable.class);

	private final Authenticator<String, Credentials> authenticator;
	private final boolean required;

	protected AuthInjectable(Authenticator<String, Credentials> authenticator, boolean required) {
		this.authenticator = authenticator;
		this.required = required;
	}

	@Override
	public Credentials getValue(HttpContext c) {

		Cookie tokenList = c.getRequest().getCookies().get("auth-token");
		if (tokenList == null || tokenList.getValue().length() == 0) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}

		String token = tokenList.getValue();
		if (token == null || token.length() == 0) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}

//			if((System.currentTimeMillis()-client.getLastRequest()) >= SESSION_TIMEOUT)   //TODO: validate Token taken from client
//				throw new WebApplicationException(Response.Status.UNAUTHORIZED);

		//Validate old token for auth token
		try {

			String sentence = token;
			if (sentence != null) {
				Optional<Credentials> result = authenticator.authenticate(sentence);
				if (result.isPresent()) {
					return result.get();

				}


			}
		} catch (IllegalArgumentException e) {
			LOGGER.debug("BasicPair decoding credentials", e);
		} catch (AuthenticationException e) {
			LOGGER.warn("BasicPair authenticating credentials", e);
			throw new WebApplicationException(Response.Status.PRECONDITION_FAILED);
		}

		if (required) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		return null;
	}
}