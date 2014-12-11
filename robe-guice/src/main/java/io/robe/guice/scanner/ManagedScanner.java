package io.robe.guice.scanner;

import com.google.inject.Injector;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
/**
 * Collects all classes extended {@link io.dropwizard.lifecycle.Managed} and adds them to environment
 *
 */
public class ManagedScanner implements Scanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<? extends Managed>> managedClasses = reflections.getSubTypesOf(Managed.class);
        for (Class<? extends Managed> managed : managedClasses) {
            environment.lifecycle().manage(injector.getInstance(managed));
            LOGGER.info("Added managed: " + managed);
        }
    }
}
