package io.robe.admin;

import com.google.inject.Module;
import io.dropwizard.Application;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.robe.admin.cli.InitializeCommand;
import io.robe.admin.guice.module.AuthenticatorModule;
import io.robe.admin.guice.module.HibernateModule;
import io.robe.admin.guice.module.MailModule;
import io.robe.admin.guice.module.QuartzModule;
import io.robe.auth.tokenbased.TokenBasedAuthBundle;
import io.robe.common.asset.ConfiguredAssetBundle;
import io.robe.common.exception.RobeExceptionMapper;
import io.robe.guice.GuiceBundle;
import io.robe.hibernate.HibernateBundle;
import io.robe.mail.MailBundle;
import io.robe.quartz.QuartzBundle;
import io.robe.quartz.hibernate.ByHibernate;

import java.util.LinkedList;
import java.util.List;


/**
 * Default io.robe.admin class of Robe.
 * If you extend this class on your applications io.robe.admin class and call super methods at
 * overridden methods you will still benefit of robe souse.
 */
public class RobeApplication<T extends RobeServiceConfiguration> extends Application<T> {


<<<<<<< HEAD
    private HibernateBundle<T> hibernateBundle = null;

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
        hibernateBundle = new HibernateBundle<T>();
        QuartzBundle<T> quartzBundle = new QuartzBundle<T>();
        MailBundle<T> mailBundle = new MailBundle<T>();
        TokenBasedAuthBundle<T> authBundle = new TokenBasedAuthBundle<T>();

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(authBundle);
        bootstrap.addBundle(quartzBundle);
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(mailBundle);
        bootstrap.addBundle(new ConfiguredAssetBundle<T>());

        List<Module> modules = new LinkedList<Module>();
        modules.add(new HibernateModule(hibernateBundle));
        modules.add(new AuthenticatorModule(authBundle, bootstrap.getMetricRegistry()));
        modules.add(new QuartzModule(quartzBundle));
        modules.add(new MailModule(mailBundle));

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
//        ResourceConfig jrConfig = environment.jersey().getResourceConfig();
//        Set<Object> dwSingletons = jrConfig.getSingletons();
//        List<Object> singletonsToRemove = new ArrayList<Object>();
//        for (Object s : dwSingletons) {
//            if (s instanceof ExceptionMapper && s.getClass().getName().startsWith("io.dropwizard.jersey.InvalidEntityExceptionMapper")) {
//                singletonsToRemove.add(s);
//            }
//        }
//
//        for (Object s : singletonsToRemove) {
//            jrConfig.getSingletons().remove(s);
//        }
        environment.jersey().register(new RobeExceptionMapper());

    }
=======
	private HibernateBundle<T> hibernateBundle = null;

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
		hibernateBundle = new HibernateBundle<T>();
		QuartzBundle<T> quartzBundle = new QuartzBundle<T>();
		MailBundle<T> mailBundle = new MailBundle<T>();
		TokenBasedAuthBundle<T> authBundle = new TokenBasedAuthBundle<T>();

		bootstrap.addBundle(hibernateBundle);
		bootstrap.addBundle(authBundle);
		bootstrap.addBundle(quartzBundle);
		bootstrap.addBundle(new ViewBundle());
		bootstrap.addBundle(mailBundle);
		bootstrap.addBundle(new ConfiguredAssetBundle<T>());

		List<Module> modules = new LinkedList<Module>();
		modules.add(new HibernateModule(hibernateBundle));
		modules.add(new AuthenticatorModule(authBundle, bootstrap.getMetricRegistry()));
		modules.add(new QuartzModule(quartzBundle));
		modules.add(new MailModule(mailBundle));

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
		RobeConfigurationManager.getInstance().setGuiceConfiguration(configuration.getGuiceConfiguration());
	}

	private void addExceptionMappers(Environment environment) {
		environment.jersey().register(new RobeExceptionMapper());

	}
>>>>>>> 4df597b9b796dbae11e71768b12dad135a55e899

}
