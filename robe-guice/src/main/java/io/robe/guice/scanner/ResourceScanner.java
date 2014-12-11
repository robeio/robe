package io.robe.guice.scanner;

import com.google.inject.Injector;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import java.util.Set;

/**
 * Collects all classes annotated by {@link javax.ws.rs.Path} and registers them to jersey.
 */
public class ResourceScanner  implements Scanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        for (Class<?> resource : resourceClasses) {
            environment.jersey().register(resource);
            LOGGER.info("Added resource class: " + resource);
        }
    }
}
