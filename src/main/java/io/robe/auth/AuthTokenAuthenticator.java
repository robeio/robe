package io.robe.auth;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.dao.UserDao;
import io.robe.hibernate.entity.User;
import org.owasp.esapi.crypto.CryptoToken;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.ValidationException;

public class AuthTokenAuthenticator implements Authenticator<String, Credentials> {



	UserDao userDao;

	@Inject
	public AuthTokenAuthenticator(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	@UnitOfWork
	public Optional<Credentials> authenticate(String token) throws AuthenticationException {

		try {
			CryptoToken cryptoToken = new CryptoToken(token);
			if (cryptoToken == null) {
				return Optional.absent();
			}
			Optional<User> user = userDao.findByEmail(cryptoToken.getUserAccountName());
			if (!user.isPresent())
				return Optional.absent();

			if (user.get().getEmail().equals(cryptoToken.getUserAccountName())) {
				Credentials credentials = new Credentials(user.get().getEmail(), user.get().getPassword());
				return Optional.fromNullable(credentials);
			}
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
		return Optional.absent();

	}

	public static CryptoToken createToken(Credentials credentials) throws ValidationException {
		CryptoToken cryptoToken = null;
		cryptoToken = new CryptoToken();
		cryptoToken.setUserAccountName(credentials.getUsername());
		cryptoToken.setExpiration(600);
		return cryptoToken;
	}
}


