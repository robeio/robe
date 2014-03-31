package io.robe.guice;

import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.util.Modules;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.spi.inject.InjectableProvider;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.lifecycle.Managed;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.core.HealthCheck;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GuiceBundle<T extends Configuration & HasGuiceConfiguration > implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceBundle.class);
    private Reflections reflections;
    private List<Module> modules = new LinkedList<Module>();
    Injector injector;


    public GuiceBundle(List<Module> modules) {
        Preconditions.checkNotNull(modules);
        this.modules = modules;
    }


    /**
     * Initializes the environment.
     *
     * @param configuration the configuration object
     * @param environment   the io.robe.admin's {@link com.yammer.dropwizard.config.Environment}
     */
    @Override
    public void run(T configuration, Environment environment) {
        try {
            if (configuration.getGuiceConfiguration() == null) {
                LOGGER.error("GuiceBundle can not work without and configuration!");
            }
            createReflections(configuration.getGuiceConfiguration().getScanPackages());
            createInjector(configuration, environment);
            addProviders(environment);
            addHealthChecks(environment, injector);
            addInjectableProviders(environment);
            addResources(environment);
            addTasks(environment, injector);
            addManaged(environment, injector);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }

    }

    private void createInjector(T configuration, Environment environment) {
        GuiceContainer container = new GuiceContainer();
        JerseyContainerModule jerseyContainerModule = new JerseyContainerModule(container);
        DropwizardEnvironmentModule dropwizardEnvironmentModule = new DropwizardEnvironmentModule<T>((Class<T>) configuration.getClass());
        modules.add(0, Modules.override(new JerseyServletModule()).with(jerseyContainerModule));
        modules.add(dropwizardEnvironmentModule);
        injector = Guice.createInjector(modules);

        container.setResourceConfig(environment.getJerseyResourceConfig());
        environment.setJerseyServletContainer(container);
        environment.addFilter(GuiceFilter.class, configuration.getHttpConfiguration().getRootPath());
        dropwizardEnvironmentModule.setEnvironmentData(configuration, environment);
    }

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
     * Initializes the io.robe.admin bootstrap.
     *
     * @param bootstrap the io.robe.admin bootstrap
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        LOGGER.trace("This is an empty method.");
    }

    private void addTasks(Environment environment, Injector injector) {
        Set<Class<? extends Task>> taskClasses = reflections.getSubTypesOf(Task.class);
        for (Class<? extends Task> task : taskClasses) {
            environment.addTask(injector.getInstance(task));
            LOGGER.info("Added task: " + task);
        }
    }

    private void addHealthChecks(Environment environment, Injector injector) {
        Set<Class<? extends HealthCheck>> healthCheckClasses = reflections.getSubTypesOf(HealthCheck.class);
        for (Class<? extends HealthCheck> healthCheck : healthCheckClasses) {
            environment.addHealthCheck(injector.getInstance(healthCheck));
            LOGGER.info("Added healthCheck: " + healthCheck);
        }
    }

    @SuppressWarnings("rawtypes")
    private void addInjectableProviders(Environment environment) {
        Set<Class<? extends InjectableProvider>> injectableProviders = reflections.getSubTypesOf(InjectableProvider.class);
        for (Class<? extends InjectableProvider> injectableProvider : injectableProviders) {
            environment.addProvider(injectableProvider);
            LOGGER.info("Added injectableProvider: " + injectableProvider);
        }
    }

    private void addProviders(Environment environment) {
        Set<Class<?>> providerClasses = reflections.getTypesAnnotatedWith(Provider.class);
        for (Class<?> provider : providerClasses) {
            environment.addProvider(provider);
            LOGGER.info("Added provider class: " + provider);
        }
    }

    private void addResources(Environment environment) {
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        for (Class<?> resource : resourceClasses) {
            environment.addResource(resource);
            LOGGER.info("Added resource class: " + resource);
        }
    }
    
    private void addManaged(Environment environment, Injector injector) {
        Set<Class<? extends Managed>> managedClasses = reflections.getSubTypesOf(Managed.class);
        for (Class<? extends Managed> managed : managedClasses) {
            environment.manage(injector.getInstance(managed));
            LOGGER.info("Added managed: " + managed);
        }
    }

}
