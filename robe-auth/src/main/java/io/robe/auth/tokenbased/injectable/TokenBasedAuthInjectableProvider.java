package io.robe.auth.tokenbased.injectable;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import io.dropwizard.auth.Auth;
import io.dropwizard.auth.Authenticator;
import io.robe.auth.Token;
import io.robe.auth.TokenFactory;
import io.robe.auth.tokenbased.BasicToken;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;

import javax.inject.Inject;

/**
 * Injectable provider for {@link io.dropwizard.auth.Auth} annotation.
 * {@inheritDoc}
 *
 * @param <T> Type of the injectable parameter.
 */
public class TokenBasedAuthInjectableProvider<T extends Token> implements InjectableProvider<Auth, Parameter> {

    private final TokenBasedAuthConfiguration configuration;
    private final Authenticator authenticator;

    /**
     * Creates a new TokenBasedAuthInjectableProvider with the given {@link Authenticator}
     *
     * @param authenticator Desired Authenticator to provide.
     */
    @Inject
    public TokenBasedAuthInjectableProvider(Authenticator authenticator, TokenBasedAuthConfiguration configuration) {
        this.authenticator = authenticator;
        this.configuration = configuration;
        try {
            TokenFactory.<BasicToken>configure(BasicToken.class, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }


    /**
     * {@inheritDoc}
     *
     * @param context   context of the component
     * @param auth      annotation
     * @param parameter Injectable parameter
     * @return Returns an instance of {@link TokenBasedAuthInjectable}.
     */
    @Override
    public Injectable getInjectable(ComponentContext context, Auth auth, Parameter parameter) {
        return new TokenBasedAuthInjectable<T>(authenticator, configuration);
    }
}
