package io.robe.auth;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.owasp.esapi.crypto.CryptoToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthTokenResponseFilter implements ContainerResponseFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthTokenResponseFilter.class);

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		String cookie = null;
		if (request.getPath().equals("authentication/login")) {
			try {
				if(response.getEntity() instanceof  Credentials){
					CryptoToken cryptoToken = AuthTokenAuthenticator.createToken(((Credentials) response.getEntity()));
					cookie = "auth-token=" + cryptoToken.getToken();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			cookie = extractCookie(request.getHeaderValue("Cookie"));
		}
		if (cookie != null || !cookie.equals("auth-token")) {
			String token = cookie.substring(11);
			if (token == null || token.equals("")) {
				return response;
			}
			try {
				CryptoToken cryptoToken = new CryptoToken(token);
				if (cryptoToken.isExpired()) {
					System.err.print("Oooo--------");
					token = cryptoToken.getToken();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			response.getHttpHeaders().putSingle("Set-Cookie", "auth-token=" + token + ";path=/;domain=" + request.getBaseUri().getHost() + ";");
		}
		return response;

	}

	public String extractCookie(String allcookies) {
		if (allcookies == null || allcookies.length() == 0)
			return null;
		String[] cookies = allcookies.split(";");
		for (String cookie : cookies) {
			if (cookie.startsWith("auth-token"))
				return cookie;
		}
		return null;
	}
}
