package io.robe.service;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.auth.AuthTokenResponseFilter;
import io.robe.cli.InitializeCommand;
import io.robe.exception.RobeExceptionMapper;
import io.robe.hibernate.HibernateBundle;

import javax.ws.rs.ext.ExceptionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * Default service class of Robe.
 * If you extend this class on your applications service class and call super methods at
 * overridden methods you will still benefit of robe souse.
 */
public class RobeService extends Service<RobeServiceConfiguration> {


	public static void main(String[] args) throws Exception {
		new RobeService().run(args);
	}

	/**
	 * Adds
	 * Hibernate bundle for DB connection
	 * Asset bundle for admin screens and
	 * Class scanners for
	 * <ul>
	 * <li>Entities</li>
	 * <li>HealthChecks</li>
	 * <li>Providers</li>
	 * <li>InjectableProviders</li>
	 * <li>Resources</li>
	 * <li>Tasks</li>
	 * <li>Managed objects</li>
	 * </ul>
	 *
	 * @param bootstrap
	 */
	@Override
	public void initialize(Bootstrap<RobeServiceConfiguration> bootstrap) {
		HibernateBundle hibernate = new HibernateBundle();

		bootstrap.addBundle(hibernate);
		bootstrap.addBundle(new NamedAssetsBundle("/admin/", "/admin", "index.html", "admin"));
		bootstrap.addBundle(GuiceBundle.newBuilder()
				.addModule(new ConfigurationModule(hibernate))
				.enableAutoConfig("io")
				.build()
		);
        bootstrap.addCommand(new InitializeCommand(this,hibernate));
    }


	/**
	 * {@inheritDoc}
	 * In addition adds exception mapper.
	 * @param configuration
	 * @param environment
	 * @throws Exception
	 */
	@UnitOfWork
	@Override
	public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
		addExceptionMappers(environment);
		environment.getJerseyResourceConfig().getContainerResponseFilters().add(new AuthTokenResponseFilter());
	}

	private void addExceptionMappers(Environment environment) {
		ResourceConfig jrConfig = environment.getJerseyResourceConfig();
		Set<Object> dwSingletons = jrConfig.getSingletons();
		List<Object> singletonsToRemove = new ArrayList<Object>();
		for (Object s : dwSingletons) {
			if (s instanceof ExceptionMapper && s.getClass().getName().startsWith("com.yammer.dropwizard.jersey.InvalidEntityExceptionMapper")) {
				singletonsToRemove.add(s);
			}
		}

		for (Object s : singletonsToRemove) {
			jrConfig.getSingletons().remove(s);
		}

		environment.addProvider(new RobeExceptionMapper());

	}
}
