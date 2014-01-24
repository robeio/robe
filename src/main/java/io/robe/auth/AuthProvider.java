package io.robe.auth;

import com.google.inject.Inject;
import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.dropwizard.auth.Authenticator;

public class AuthProvider<T> implements InjectableProvider<Auth, Parameter> {

	@Inject
	private  Authenticator authenticator;

	/**
	 * Creates a new AuthProvider with the given {@link Authenticator}
	 */
	public AuthProvider() {
	}

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

	@Override
	public Injectable<?> getInjectable(ComponentContext ic, Auth a, Parameter c) {
		return new AuthInjectable<T>(authenticator, a.required());
	}
}
