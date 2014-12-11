package io.robe.guice.scanner;

import com.google.inject.Injector;
import com.sun.jersey.spi.inject.InjectableProvider;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Collects all classes extended {@link com.sun.jersey.spi.inject.InjectableProvider} and registers them to jersey
 *
 */
public class InjectableProviderScanner implements Scanner{
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectableProviderScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<? extends InjectableProvider>> injectableProviders = reflections.getSubTypesOf(InjectableProvider.class);
        for (Class<? extends InjectableProvider> injectableProvider : injectableProviders) {
            environment.jersey().register(injectableProvider);
            LOGGER.info("Added injectableProvider: " + injectableProvider);
        }
    }
}
