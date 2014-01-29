package io.robe.service;

import com.google.common.cache.CacheBuilderSpec;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import io.robe.auth.AuthTokenAuthenticator;
import io.robe.auth.Credentials;
import io.robe.hibernate.HibernateBundle;
import io.robe.hibernate.dao.ServiceDao;
import io.robe.hibernate.dao.UserDao;
import org.hibernate.SessionFactory;

public class ConfigurationModule extends AbstractModule {

	HibernateBundle hibernate ;

	public ConfigurationModule(HibernateBundle hibernate) {
		this.hibernate = hibernate;
	}

	@Override
	protected void configure() {
		bind(SessionFactory.class).toProvider(new Provider<SessionFactory>() {
			@Override
			public  SessionFactory get() {
				return hibernate.getSessionFactory();
			}
		});

		bind(Authenticator.class).toProvider(new Provider<Authenticator<String, Credentials> >() {
			@Override
			public Authenticator get() {
				AuthTokenAuthenticator authTokenAuthenticator = new AuthTokenAuthenticator(new UserDao(hibernate.getSessionFactory()), new ServiceDao(hibernate.getSessionFactory()));
				return CachingAuthenticator.wrap(authTokenAuthenticator, CacheBuilderSpec.parse("maximumSize=10000, expireAfterAccess=1m"));
			}
		});

	}
	

}
