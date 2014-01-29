package io.robe.service;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import io.robe.audit.AuditedMethodDispatchProvider;
import io.robe.auth.AuthTokenResponseFilter;
import io.robe.exception.RobeExceptionMapper;
import io.robe.hibernate.HibernateBundle;

import javax.ws.rs.ext.ExceptionMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class RobeService extends Service<RobeServiceConfiguration> {


	public static void main(String[] args) throws Exception {
		new RobeService().run(args);

	}


	@Override
	public void initialize(Bootstrap<RobeServiceConfiguration> bootstrap) {
		HibernateBundle hibernate = new HibernateBundle();

		bootstrap.addBundle(hibernate);
		bootstrap.addBundle(new AssetsBundle("/www/", "/", "index.html"));
		bootstrap.addBundle(GuiceBundle.newBuilder()
				.addModule(new ConfigurationModule(hibernate))
				.enableAutoConfig("io")
				.build()
		);
	}


	@UnitOfWork
	@Override
	public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
		addExceptionMappers(environment);
		environment.getJerseyResourceConfig().getContainerResponseFilters().add(new AuthTokenResponseFilter());
		environment.addProvider(AuditedMethodDispatchProvider.AuditedMethodDispatchAdapter.class);
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
