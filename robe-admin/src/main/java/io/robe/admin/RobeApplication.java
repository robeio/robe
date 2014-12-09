package io.robe.admin;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import io.dropwizard.Application;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.dropwizard.views.ViewRenderer;
import io.dropwizard.views.freemarker.FreemarkerViewRenderer;
import io.robe.admin.cli.InitializeCommand;
import io.robe.admin.guice.module.AuthenticatorModule;
import io.robe.admin.guice.module.HibernateModule;
import io.robe.admin.guice.module.QuartzModule;
import io.robe.auth.tokenbased.TokenBasedAuthBundle;
import io.robe.assets.ConfiguredAssetBundle;
import io.robe.common.exception.RobeExceptionMapper;
import io.robe.guice.GuiceBundle;
import io.robe.hibernate.HibernateBundle;
import io.robe.mail.MailBundle;
import io.robe.quartz.QuartzBundle;
import io.robe.quartz.job.hibernate.ByHibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;


/**
 * Default io.robe.admin class of Robe.
 * If you extend this class on your applications io.robe.admin class and call super methods at
 * overridden methods you will still benefit of robe souse.
 */
public class RobeApplication<T extends RobeServiceConfiguration> extends Application<T> {


	private HibernateBundle<T> hibernateBundle = null;
	private static final Logger LOGGER = LoggerFactory.getLogger(RobeApplication.class);

	public static void main(String[] args) throws Exception {
		new RobeApplication().run(args);
	}

	public HibernateBundle getHibernateBundle() {
		return hibernateBundle;
	}

	/**
	 * Adds
	 * Hibernate bundle for PROVIDER connection
	 * Asset bundle for io.robe.admin screens and
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
	public void initialize(Bootstrap<T> bootstrap) {
		LOGGER.info("Robe Admin is Starting...");
		hibernateBundle = new HibernateBundle<T>();
		QuartzBundle<T> quartzBundle = new QuartzBundle<T>();
		MailBundle<T> mailBundle = new MailBundle<T>();
		TokenBasedAuthBundle<T> authBundle = new TokenBasedAuthBundle<T>();

		bootstrap.addBundle(hibernateBundle);
		bootstrap.addBundle(authBundle);
		bootstrap.addBundle(quartzBundle);
		bootstrap.addBundle(new ViewBundle());
		bootstrap.addBundle(new ViewBundle(ImmutableList.<ViewRenderer>of(new FreemarkerViewRenderer())));
		bootstrap.addBundle(mailBundle);
		bootstrap.addBundle(new ConfiguredAssetBundle<T>());

		List<Module> modules = new LinkedList<Module>();
		modules.add(new HibernateModule(hibernateBundle));
		modules.add(new AuthenticatorModule(authBundle, bootstrap.getMetricRegistry()));
		modules.add(new QuartzModule(quartzBundle));

		bootstrap.addBundle(new GuiceBundle<T>(modules, bootstrap.getApplication().getConfigurationClass()));
		bootstrap.addCommand(new InitializeCommand(this, hibernateBundle));


		//TODO: Bad way to get it. Will change it later.
		ByHibernate.setHibernateBundle(hibernateBundle);

	}


	/**
	 * {@inheritDoc}
	 * In addition adds exception mapper.
	 *
	 * @param configuration
	 * @param environment
	 * @throws Exception
	 */
	@UnitOfWork
	@Override
	public void run(T configuration, Environment environment) throws Exception {
		addExceptionMappers(environment);
	}

	private void addExceptionMappers(Environment environment) {
		environment.jersey().register(new RobeExceptionMapper());

	}

}
