package io.robe.auth;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.hibernate.dao.ServiceDao;
import io.robe.hibernate.dao.UserDao;
import io.robe.hibernate.entity.Permission;
import io.robe.hibernate.entity.Service;
import io.robe.hibernate.entity.User;
import org.owasp.esapi.crypto.CryptoToken;
import org.owasp.esapi.errors.EncryptionException;
import org.owasp.esapi.errors.ValidationException;

import java.util.Collections;
import java.util.HashSet;

/**
 * Authenticator implemented for auth-token
 */
public class AuthTokenAuthenticator implements Authenticator<String, Credentials> {



	ServiceDao serviceDao;
	UserDao userDao;

	/**
	 * Creates an instance of AuthTokenAuthenticator with the dao classes.
	 *
	 * @param userDao    User DAO for authenticating user
	 * @param serviceDao Service DAO for permission list
	 */
	@Inject
	public AuthTokenAuthenticator(UserDao userDao,ServiceDao serviceDao) {
		this.userDao = userDao;
		this.serviceDao = serviceDao;
	}

	/**
	 * Creates {@link com.google.common.base.Optional} {@link io.robe.auth.Credentials} instance from provided auth-token
	 * @param token Auth-Token to decode.
	 * @return Optional instance of a {@link io.robe.auth.Credentials} which created from token
	 * @throws AuthenticationException
	 */
	@Override
	@UnitOfWork
	public Optional<Credentials> authenticate(String token) throws AuthenticationException {

		try {
			if (token == null) {
				return Optional.absent();
			}
			// All ok decode token and check user credentials
			CryptoToken cryptoToken = new CryptoToken(token);
			Optional<User> user = userDao.findByEmail(cryptoToken.getUserAccountName());
			if (!user.isPresent())
				return Optional.absent();

			// If access granted collect users Service Permissions for authorization controls
			if (user.get().isActive() && user.get().getEmail().equals(cryptoToken.getUserAccountName())) {
				HashSet<String> permissions = new HashSet<String>();
				for(Permission permission:user.get().getRole().getPermissions()){
					if(permission.getType().equals(Permission.Type.SERVICE)){
						Service service = serviceDao.findById(permission.getRestrictedItemOid());
						permissions.add(service.getPath() + ":"+service.getMethod());
					}
				}
				Credentials credentials = new Credentials(user.get().getEmail(), user.get().getPassword(),Collections.unmodifiableSet(permissions));
				return Optional.fromNullable(credentials);
			}
		} catch (EncryptionException e) {
			e.printStackTrace();
		}
		return Optional.absent();

	}

	/**
	 * Creates an access token with the given {@link io.robe.auth.Credentials}
	 *
	 * @param credentials {@link io.robe.auth.Credentials} instance provided to create token.
	 * @return {@link org.owasp.esapi.crypto.CryptoToken} instance with 10min. expiration.
	 * @throws ValidationException
	 */
	protected static CryptoToken createToken(Credentials credentials) throws ValidationException {
		CryptoToken cryptoToken = new CryptoToken();
		cryptoToken.setUserAccountName(credentials.getUsername());
		cryptoToken.setExpiration(600);
		return cryptoToken;
	}
}


