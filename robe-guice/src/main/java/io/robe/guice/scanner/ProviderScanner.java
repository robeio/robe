package io.robe.guice.scanner;

import com.google.inject.Injector;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ext.Provider;
import java.util.Set;

/**
 * Collects all classes annotated by {@link javax.ws.rs.ext.Provider} and registers them to jersey
 */
public class ProviderScanner implements Scanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<?>> providerClasses = reflections.getTypesAnnotatedWith(Provider.class);
        for (Class<?> provider : providerClasses) {
            environment.jersey().register(provider);
            LOGGER.info("Added provider class: " + provider);
        }
    }
}
