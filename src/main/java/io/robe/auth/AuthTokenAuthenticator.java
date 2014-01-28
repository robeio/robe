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

public class AuthTokenAuthenticator implements Authenticator<String, Credentials> {



	ServiceDao serviceDao;
	UserDao userDao;

	@Inject
	public AuthTokenAuthenticator(UserDao userDao,ServiceDao serviceDao) {
		this.userDao = userDao;
		this.serviceDao = serviceDao;
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

	public static CryptoToken createToken(Credentials credentials) throws ValidationException {
		CryptoToken cryptoToken = null;
		cryptoToken = new CryptoToken();
		cryptoToken.setUserAccountName(credentials.getUsername());
		cryptoToken.setExpiration(600);
		return cryptoToken;
	}
}


