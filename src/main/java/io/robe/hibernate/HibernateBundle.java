package io.robe.hibernate;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.ConfigurationStrategy;
import com.yammer.dropwizard.hibernate.SessionFactoryFactory;
import com.yammer.dropwizard.hibernate.SessionFactoryHealthCheck;
import com.yammer.dropwizard.hibernate.UnitOfWorkResourceMethodDispatchAdapter;
import io.robe.hibernate.entity.BaseEntity;
import io.robe.service.RobeServiceConfiguration;
import org.hibernate.SessionFactory;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Hibernate bundle with Entity classpath scanner.
 * Takes scannable paths from configuration yml from entityPackage element as array separated by ','
 */
public class HibernateBundle implements ConfiguredBundle<RobeServiceConfiguration>, ConfigurationStrategy<RobeServiceConfiguration> {
	private SessionFactory sessionFactory;
	private final SessionFactoryFactory sessionFactoryFactory = new SessionFactoryFactory();

	public HibernateBundle() {

	}


	@Override
	public final void initialize(Bootstrap<?> bootstrap) {
		bootstrap.getObjectMapperFactory().registerModule(new Hibernate4Module());
	}

	/**
	 * Reads the configuration and builds the session factory, with entities.
	 *
	 * @param configuration configuration to read
	 * @param environment   environment to add
	 * @throws Exception
	 */
	@Override
	public final void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
		final DBConfiguration dbConfig = getDatabaseConfiguration(configuration);
		this.sessionFactory = sessionFactoryFactory.build(environment, dbConfig, getEntities(dbConfig.getEntityPackage().split(",")));
		environment.addProvider(new UnitOfWorkResourceMethodDispatchAdapter(sessionFactory));
		environment.addHealthCheck(new SessionFactoryHealthCheck("hibernate", sessionFactory, dbConfig.getValidationQuery()));
 	}

	private List<Class<?>> getEntities(String[] packages) {

		Set<Class<?>> classes = new HashSet<Class<?>>();
		for(String package1 : packages){
			Reflections reflections = new Reflections(package1);
			classes.addAll(reflections.getTypesAnnotatedWith(Entity.class));
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
	public DBConfiguration getDatabaseConfiguration(RobeServiceConfiguration configuration) {
		return configuration.getDatabaseConfiguration();
	}

}


