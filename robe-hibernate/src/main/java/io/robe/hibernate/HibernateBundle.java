package io.robe.hibernate;

import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.hibernate.SessionFactoryHealthCheck;
import io.dropwizard.hibernate.UnitOfWorkResourceMethodDispatchAdapter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.robe.hibernate.entity.BaseEntity;
import org.hibernate.SessionFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Hibernate bundle with Entity classpath scanner.
 * Takes scannable paths from configuration yml from entityPackage element as array separated by ','
 */
public class HibernateBundle<T extends Configuration & HasHibernateConfiguration> implements ConfiguredBundle<T>, DatabaseConfiguration<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateBundle.class);

    private SessionFactory sessionFactory;
    private final RobeSessionFactoryFactory sessionFactoryFactory = new RobeSessionFactoryFactory();


    /**
     * Reads the configuration and builds the session factory, with entities.
     *
     * @param configuration configuration to read
     * @param environment   environment to add
     * @throws ClassNotFoundException
     */
    @Override
    public final void run(T configuration, Environment environment) throws Exception {
        final HibernateConfiguration databaseConfiguration = getDatabaseConfiguration(configuration);
        final DataSourceFactory dbConfig = getDataSourceFactory(configuration);
        LOGGER.info("\n------------------------\n----Hibernate Bundle----\n------------------------");
        LOGGER.info("Creating Hibernate SessionFactory");
        this.sessionFactory = sessionFactoryFactory.build(this, environment, dbConfig,
                getEntities(databaseConfiguration.getScanPackages(), databaseConfiguration.getEntities()));
        environment.jersey().register(new UnitOfWorkResourceMethodDispatchAdapter(sessionFactory));
        environment.healthChecks().register("hibernate",
                new SessionFactoryHealthCheck(sessionFactory,
                        dbConfig.getValidationQuery()));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    private List<Class<?>> getEntities(String[] packages, String[] entities) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        if (packages != null) {
            for (String packageName : packages) {
                LOGGER.info("Loading Package: " + packageName);
                Reflections reflections = new Reflections(packageName);
                classes.addAll(reflections.getTypesAnnotatedWith(Entity.class));
            }
        }
        if (entities != null) {
            for (String entity : entities) {
                try {
                    LOGGER.info("Loading Entity: " + entity);
                    Class entityClass = Class.forName(entity);
                    if (entityClass.isAnnotationPresent(Entity.class)) {
                        classes.add(entityClass);
                    } else {
                        LOGGER.warn("Class is not annotated with Entity: " + entity);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("Can't load class: " + entity, e);
                }
            }
        }
        return ImmutableList.<Class<?>>builder().add(BaseEntity.class).addAll(classes).build();
    }


    /**
     * @return current session factory
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }


    public HibernateConfiguration getDatabaseConfiguration(T configuration) {
        return configuration.getHibernateConfiguration();
    }

    @Override
    public DataSourceFactory getDataSourceFactory(T configuration) {
        return configuration.getHibernateConfiguration().getDataSourceFactory(configuration);
    }

    public void configure(org.hibernate.cfg.Configuration configuration) {

    }
}


