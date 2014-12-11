package io.robe.guice;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.robe.guice.scanner.Scanner;
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
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GuiceBundle<T extends Configuration & HasGuiceConfiguration> implements ConfiguredBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuiceBundle.class);

    private static Injector injector = null;

    public static Injector getInjector() {
        return injector;
    }

    private Reflections reflections;
    private List<Module> modules = new LinkedList<Module>();
    GuiceContainer container;
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
            findAndRunScanners(environment, injector);


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
     *
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
     * Collects all classes extended {@link io.robe.guice.scanner.Scanner} and adds them to environment
     *
     * @param environment target environment
     * @param injector    guice injector to create instances.
     */
    private void findAndRunScanners(Environment environment, Injector injector) {
        Set<Class<? extends Scanner>> scanners = reflections.getSubTypesOf(Scanner.class);
        for (Class<? extends Scanner> scanner : scanners) {
            try {
                LOGGER.info("------------------------" + scanner);
                Scanner instance = scanner.newInstance();
                instance.scanAndAdd(environment, injector, reflections);
                LOGGER.info("------------------------");
            } catch (Exception e) {
                LOGGER.error("Added scanner: " + scanner, e);
            }
        }
    }

}
