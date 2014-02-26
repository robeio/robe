package io.robe.admin.guice.module;

import com.google.common.cache.CacheBuilderSpec;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.auth.AuthTokenAuthenticator;
import io.robe.auth.Credentials;
import io.robe.hibernate.HibernateBundle;

/**
 * Default Guice bindings are done at this class.
 */
public class AuthenticatorModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Authenticator.class).toProvider(new Provider<Authenticator<String, Credentials>>() {
            @Inject
            HibernateBundle hibernateBundle;

            @Override
            public Authenticator get() {
                AuthTokenAuthenticator authTokenAuthenticator =
                        new AuthTokenAuthenticator(
                                new UserDao(hibernateBundle.getSessionFactory()),
                                new ServiceDao(hibernateBundle.getSessionFactory()));
                return CachingAuthenticator.wrap(authTokenAuthenticator, CacheBuilderSpec.parse("maximumSize=10000, expireAfterAccess=1m"));
            }
        });

    }


}
