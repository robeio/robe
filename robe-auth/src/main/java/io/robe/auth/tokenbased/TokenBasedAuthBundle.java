package io.robe.auth.tokenbased;

import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import io.robe.auth.TokenWrapper;
import io.robe.auth.tokenbased.configuration.HasTokenBasedAuthConfiguration;
import io.robe.auth.tokenbased.configuration.TokenBasedAuthConfiguration;
import io.robe.auth.tokenbased.filter.TokenBasedAuthResourceFilterFactory;

public class TokenBasedAuthBundle<T extends Configuration & HasTokenBasedAuthConfiguration> implements ConfiguredBundle<T> {
    private TokenBasedAuthConfiguration configuration;

    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the service's {@link com.yammer.dropwizard.config.Environment}
     * @throws Exception if something goes wrong
     */
    @Override
    public void run(T configuration, Environment environment) throws Exception {
        this.configuration = configuration.getTokenBasedAuthConfiguration();
        environment.getJerseyResourceConfig().getResourceFilterFactories().add(new TokenBasedAuthResourceFilterFactory(configuration.getTokenBasedAuthConfiguration()));
        TokenWrapper.setMaxage(configuration.getTokenBasedAuthConfiguration().getMaxage());
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
