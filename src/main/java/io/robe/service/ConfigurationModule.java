package io.robe.service;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.yammer.dropwizard.auth.Authenticator;
import io.robe.auth.AuthTokenAuthenticator;
import io.robe.auth.Credentials;
import io.robe.hibernate.HibernateBundle;
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
			public Authenticator<String, Credentials>  get() {
				return new AuthTokenAuthenticator();
			}
		});
	}
	

}
