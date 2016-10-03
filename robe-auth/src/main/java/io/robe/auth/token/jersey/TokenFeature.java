package io.robe.auth.token.jersey;

import io.robe.auth.RobeAuth;
import io.robe.auth.token.TokenAuthenticator;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class TokenFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder() {
            @Override
            public void configure() {
                bind(TokenAuthenticator.class)
                        .in(Singleton.class);
                bind(TokenFactory.class)
                        .in(Singleton.class);
                bind(TokenFactoryProvider.class)
                        .to(ValueFactoryProvider.class)
                        .in(Singleton.class);
                bind(TokenParamInjectionResolver.class)
                        .to(new TypeLiteral<InjectionResolver<RobeAuth>>() {
                        })
                        .in(Singleton.class);
            }
        });
        return true;
    }
}