package io.robe.auth.tokenbased;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.robe.auth.Credentials;
import io.robe.auth.token.TokenFactoryProvider;
import io.robe.auth.token.TokenFeature;
import io.robe.auth.token.TokenManager;
import io.robe.auth.tokenbased.configuration.HasTokenBasedAuthConfiguration;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import io.robe.auth.tokenbased.filter.TokenBasedAuthResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenBasedAuthBundle<T extends Configuration & HasTokenBasedAuthConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenBasedAuthBundle.class);
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
        LOGGER.info("\n------------------------\n-------Auth Bundle------\n------------------------");
        this.configuration = configuration.getTokenBasedAuthConfiguration();

        environment.jersey().register(new TokenFactoryProvider.Binder<Credentials>(Credentials.class));
        environment.jersey().register(new TokenBasedAuthResponseFilter(configuration.getTokenBasedAuthConfiguration()));
        environment.jersey().register(TokenFeature.class);

        TokenManager.configure(BasicToken.class, configuration.getTokenBasedAuthConfiguration());

//        environment.jersey().register(AuthFactory.binder(authFactory));

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
