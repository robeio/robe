package io.robe.guice;

import com.codahale.metrics.health.HealthCheck;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import com.sun.jersey.spi.inject.InjectableProvider;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.servlet.DispatcherType;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GuiceBundle<T extends Configuration & HasGuiceConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceBundle.class);
    private Reflections reflections;
    private List<Module> modules = new LinkedList<Module>();
    GuiceContainer container;
    Injector injector;
    DropwizardEnvironmentModule deModule;
    Class<T> type;

    public GuiceBundle(List<Module> modules, Class<T> type) {
        Preconditions.checkNotNull(modules);
        this.modules = modules;
        this.type = type;

    }


    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        container = new GuiceContainer();
        JerseyContainerModule jerseyContainerModule = new JerseyContainerModule(container);
        deModule = new DropwizardEnvironmentModule<T>(type);
        modules.add(deModule);
        modules.add(jerseyContainerModule);
        injector = Guice.createInjector(Stage.PRODUCTION, modules);

    }
    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the io.robe.admin's {@link io.dropwizard.setup.Environment}
     */
    @Override
    public void run(T configuration, Environment environment) {
        LOGGER.info("------------------------");
        LOGGER.info("------Guice Bundle------");
        LOGGER.info("------------------------");
        try {
            if (configuration.getGuiceConfiguration() == null) {
                LOGGER.error("GuiceBundle can not work without and configuration!");
            }
            createReflections(configuration.getGuiceConfiguration().getScanPackages());
            prepareContainer(configuration, environment);
            addProviders(environment);
            addHealthChecks(environment, injector);
            addInjectableProviders(environment);
            addResources(environment);
            addTasks(environment, injector);
            addManaged(environment, injector);


        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

    }

    /**
     * Prepares a guice servlet container for jersey
     *
     * @param configuration
     * @param environment   target environment.
     */
    private void prepareContainer(T configuration, Environment environment) {

        container.setResourceConfig(environment.jersey().getResourceConfig());
        environment.jersey().replace(new Function<ResourceConfig, ServletContainer>() {
            @Nullable
            @Override
            public ServletContainer apply(ResourceConfig resourceConfig) {
                return container;
            }
        });
        environment.servlets().addFilter("GuiceFilter", GuiceFilter.class)
                .addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, environment.getApplicationContext().getContextPath() + "*");
        deModule.setEnvironmentData(configuration, environment);

    }

    /**
     * Creates a {@link org.reflections.Reflections} with the given packages (configuration)
     * @param scanPackages
     */
    private void createReflections(String[] scanPackages) {
        if (scanPackages.length < 1) {
            LOGGER.warn("No package defined in configuration (scanPackages)!");
            return;
        }
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        FilterBuilder filterBuilder = new FilterBuilder();
        for (String packageName : scanPackages) {
            configurationBuilder.addUrls(ClasspathHelper.forPackage(packageName));
            filterBuilder.include(FilterBuilder.prefix(packageName));
        }

        configurationBuilder.filterInputsBy(filterBuilder).setScanners(new SubTypesScanner(), new TypeAnnotationsScanner());
        this.reflections = new Reflections(configurationBuilder);

    }

    /**
     * Collects all classes extended {@link io.dropwizard.servlets.tasks.Task} and adds them to admin servlet
     * @param environment target environment
     */
    private void addTasks(Environment environment, Injector injector) {
        Set<Class<? extends Task>> taskClasses = reflections.getSubTypesOf(Task.class);
        for (Class<? extends Task> task : taskClasses) {
            environment.admin().addTask(injector.getInstance(task));
            LOGGER.info("Added task: " + task);
        }
    }

    /**
     * Collects all classes extended {@link com.codahale.metrics.health.HealthCheck} and registers them to jersey
     * @param environment target environment
     */
    private void addHealthChecks(Environment environment, Injector injector) {
        Set<Class<? extends HealthCheck>> healthCheckClasses = reflections.getSubTypesOf(HealthCheck.class);
        for (Class<? extends HealthCheck> healthCheck : healthCheckClasses) {
            environment.healthChecks().register(healthCheck.getName(), injector.getInstance(healthCheck));
            LOGGER.info("Added healthCheck: " + healthCheck.getName());
        }
    }

    /**
     * Collects all classes extended {@link com.sun.jersey.spi.inject.InjectableProvider} and registers them to jersey
     * @param environment target environment
     */
    @SuppressWarnings("rawtypes")
    private void addInjectableProviders(Environment environment) {
        Set<Class<? extends InjectableProvider>> injectableProviders = reflections.getSubTypesOf(InjectableProvider.class);
        for (Class<? extends InjectableProvider> injectableProvider : injectableProviders) {
            environment.jersey().register(injectableProvider);
            LOGGER.info("Added injectableProvider: " + injectableProvider);
        }
    }

    /**
     * Collects all classes annotated by {@link javax.ws.rs.ext.Provider} and registers them to jersey
     * @param environment target environment
     */
    private void addProviders(Environment environment) {
        Set<Class<?>> providerClasses = reflections.getTypesAnnotatedWith(Provider.class);
        for (Class<?> provider : providerClasses) {
            environment.jersey().register(provider);
            LOGGER.info("Added provider class: " + provider);
        }
    }

    /**
     * Collects all classes annotated by {@link javax.ws.rs.Path} and registers them to jersey.
     * @param environment target environment
     */
    private void addResources(Environment environment) {
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        for (Class<?> resource : resourceClasses) {
            environment.jersey().register(resource);
            LOGGER.info("Added resource class: " + resource);
        }
    }

    /**
     * Collects all classes extended {@link io.dropwizard.lifecycle.Managed} and adds them to environment
     * @param environment target environment
     * @param injector guice injector to create instances.
     */
    private void addManaged(Environment environment, Injector injector) {
        Set<Class<? extends Managed>> managedClasses = reflections.getSubTypesOf(Managed.class);
        for (Class<? extends Managed> managed : managedClasses) {
            environment.lifecycle().manage(injector.getInstance(managed));
            LOGGER.info("Added managed: " + managed);
        }
    }

}
