package io.robe.guice.scanner;

import com.codahale.metrics.health.HealthCheck;
import com.google.inject.Injector;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
/**
 * Collects all classes extended {@link com.codahale.metrics.health.HealthCheck} and registers them to jersey
 */
public class HealthCheckScanner implements Scanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckScanner.class);
    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<? extends HealthCheck>> healthCheckClasses = reflections.getSubTypesOf(HealthCheck.class);
        for (Class<? extends HealthCheck> healthCheck : healthCheckClasses) {
            environment.healthChecks().register(healthCheck.getName(), injector.getInstance(healthCheck));
            LOGGER.info("Added healthCheck: " + healthCheck.getName());
        }
    }
}
