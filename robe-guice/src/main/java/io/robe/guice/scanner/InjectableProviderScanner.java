package io.robe.guice.scanner;

import com.google.inject.Injector;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.InjectionResolver;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Collects all classes extended {@link org.glassfish.hk2.api.InjectionResolver} and registers them to jersey
 *
 */
public class InjectableProviderScanner implements Scanner{
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectableProviderScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<? extends InjectionResolver>> resolvers = reflections.getSubTypesOf(InjectionResolver.class);
        for (Class<? extends InjectionResolver> resolver : resolvers) {
            //TODO: Check if it is valid usage.
            environment.jersey().register(resolver);
            LOGGER.info("Added InjectableResolver: " + resolver);
        }
    }
}
