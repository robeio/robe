package io.robe.hibernate;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.ConfigurationStrategy;
import com.yammer.dropwizard.hibernate.SessionFactoryFactory;
import com.yammer.dropwizard.hibernate.SessionFactoryHealthCheck;
import com.yammer.dropwizard.hibernate.UnitOfWorkResourceMethodDispatchAdapter;
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
public class HibernateBundle<T extends Configuration & HasHibernateConfiguration> implements ConfiguredBundle<T>, ConfigurationStrategy<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateBundle.class);

    private SessionFactory sessionFactory;
	private final SessionFactoryFactory sessionFactoryFactory = new SessionFactoryFactory();


	@Override
	public final void initialize(Bootstrap<?> bootstrap) {
		bootstrap.getObjectMapperFactory().registerModule(new Hibernate4Module());
	}

	/**
	 * Reads the configuration and builds the session factory, with entities.
	 *
	 * @param configuration configuration to read
	 * @param environment   environment to add
     * @throws ClassNotFoundException
     */
	@Override
    public final void run(T configuration, Environment environment) throws ClassNotFoundException {
        final HibernateConfiguration databaseConfiguration = getDatabaseConfiguration(configuration);
		this.sessionFactory = sessionFactoryFactory.build(environment, databaseConfiguration,
                getEntities(databaseConfiguration.getScanPackages(),
                databaseConfiguration.getEntities()));
		environment.addProvider(new UnitOfWorkResourceMethodDispatchAdapter(sessionFactory));
		environment.addHealthCheck(new SessionFactoryHealthCheck("hibernate", sessionFactory, databaseConfiguration.getValidationQuery()));
 	}

	private List<Class<?>> getEntities(String[] packages,String[] entities) {

		Set<Class<?>> classes = new HashSet<Class<?>>();
        if(packages != null){
            for (String packageName : packages) {
                Reflections reflections = new Reflections(packageName);
                classes.addAll(reflections.getTypesAnnotatedWith(Entity.class));
            }
        }
        if(entities != null){
            for(String entity : entities){
                try {
                    Class entityClass = Class.forName(entity);
                    if (entityClass.isAnnotationPresent(Entity.class)) {
                        classes.add(entityClass);
                    } else {
                        LOGGER.warn("Class is not annotated with Entity: " + entity);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("Can't load class: " + entity , e);
                }
            }
        }
		return  ImmutableList.<Class<?>>builder().add(BaseEntity.class).addAll(classes).build();
	}

	/**
	 *
	 * @return current session factory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}


	@Override
	public HibernateConfiguration getDatabaseConfiguration(T configuration) {
		return configuration.getHibernateConfiguration();
	}

}


