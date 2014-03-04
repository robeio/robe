package io.robe.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.owasp.esapi.crypto.CryptoToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The response filter for creating or refreshing AuthTokens. Refreshing controlled by created token.
 */
public class AuthTokenResponseFilter implements ContainerResponseFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenResponseFilter.class);

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		String authToken = null;

		/*
		 If it is the first login create a token with the Credentials from response entity.
		 and get token form there.
		 Else get token from cookie
		  */
		if (request.getPath().equals("authentication/login")) {
			try {
				if(response.getEntity() instanceof  Credentials){
					CryptoToken cryptoToken = AuthTokenAuthenticator.createToken(((Credentials) response.getEntity()));
					authToken = cryptoToken.getToken();
				}
			} catch (Exception e) {
				LOGGER.error("CryptoToken creation failed", e);
			}
		} else {
			authToken = extractAuthTokenFromCookieList(request.getHeaderValue("Cookie"));
		}

		if (authToken != null) {

			if (authToken.length() == 0) {
				return response;
			}
			try {
				CryptoToken cryptoToken = new CryptoToken(authToken);
				if (cryptoToken.isExpired()) {
					authToken = cryptoToken.getToken();
				}
			} catch (Exception e) {
				LOGGER.error("CryptoToken re-creation failed", e);
			}

			response.getHttpHeaders().putSingle("Set-Cookie", "auth-token=" + authToken + ";path=/;domain=" + request.getBaseUri().getHost() + ";");
		}
		return response;

	}

	private String extractAuthTokenFromCookieList(String cookieList) {
		if (cookieList == null || cookieList.length() == 0)
			return null;
		String[] cookies = cookieList.split(";");
		for (String cookie : cookies) {
			if (cookie.startsWith("auth-token"))
				return cookie.substring(11);
		}
		return null;
	}
}
