package io.robe.auth.token;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.robe.auth.Credentials;
import io.robe.auth.token.configuration.HasTokenBasedAuthConfiguration;
import io.robe.auth.token.configuration.TokenBasedAuthConfiguration;
import io.robe.auth.token.jersey.TokenBasedAuthResponseFilter;
import io.robe.auth.token.jersey.TokenFactoryProvider;
import io.robe.auth.token.jersey.TokenFeature;

public class TokenAuthBundle<T extends Configuration & HasTokenBasedAuthConfiguration> implements ConfiguredBundle<T> {
    private TokenBasedAuthConfiguration configuration;

    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the service's {@link io.dropwizard.setup.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(T configuration, Environment environment) throws Exception {
        this.configuration = configuration.getTokenBasedAuthConfiguration();
        environment.jersey().register(new TokenFactoryProvider.Binder<Credentials>(Credentials.class));
        environment.jersey().register(new TokenBasedAuthResponseFilter(configuration.getTokenBasedAuthConfiguration()));
        environment.jersey().register(TokenFeature.class);
        BasicToken.configure(configuration.getTokenBasedAuthConfiguration());
    }

    /**
     * Initializes the service bootstrap.
     *
     * @param bootstrap the service bootstrap
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }


    public TokenBasedAuthConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(TokenBasedAuthConfiguration configuration) {
        this.configuration = configuration;
    }
}
