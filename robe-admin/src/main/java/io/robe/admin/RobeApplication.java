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
import io.robe.admin.guice.module.HibernateModule;
import io.robe.admin.hibernate.dao.ServiceDao;
import io.robe.admin.hibernate.dao.UserDao;
import io.robe.admin.quartz.hibernate.HibernateJobProvider;
import io.robe.assets.AdvancedAssetBundle;
import io.robe.auth.token.TokenAuthenticator;
import io.robe.auth.token.TokenFactory;
import io.robe.auth.tokenbased.TokenBasedAuthBundle;
import io.robe.common.exception.RobeExceptionMapper;
import io.robe.guice.GuiceBundle;
import io.robe.hibernate.RobeHibernateBundle;
import io.robe.mail.MailBundle;
import io.robe.quartz.QuartzBundle;
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


    private static final Logger LOGGER = LoggerFactory.getLogger(RobeApplication.class);


    public static void main(String[] args) throws Exception {

        RobeApplication application = new RobeApplication();
        application.run(args);

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

        RobeHibernateBundle<T> hibernateBundle = RobeHibernateBundle.createInstance(new String[]{"io.robe.admin.hibernate.entity", "io.robe.quartz"}, new String[0]);

        TokenBasedAuthBundle<T> authBundle = new TokenBasedAuthBundle<T>();
        addGuiceBundle(bootstrap, authBundle, hibernateBundle);
        bootstrap.addBundle(authBundle);

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addCommand(new InitializeCommand(this, hibernateBundle));

        //TODO: Find a better way to send it
        HibernateJobProvider.setHibernateBundle(hibernateBundle);

        bootstrap.addBundle(new QuartzBundle<T>());
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new ViewBundle(ImmutableList.<ViewRenderer>of(new FreemarkerViewRenderer())));
        bootstrap.addBundle(new MailBundle<T>());
        bootstrap.addBundle(new AdvancedAssetBundle<T>());

    }


    private void addGuiceBundle(Bootstrap<T> bootstrap, TokenBasedAuthBundle<T> authBundle, RobeHibernateBundle hibernateBundle) {
        List<Module> modules = new LinkedList<Module>();
        modules.add(new HibernateModule(hibernateBundle));

        bootstrap.addBundle(new GuiceBundle<T>(modules, bootstrap.getApplication().getConfigurationClass()));
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

        TokenFactory.authenticator = new TokenAuthenticator(
                GuiceBundle.getInjector().getInstance(UserDao.class),
                GuiceBundle.getInjector().getInstance(ServiceDao.class));
        TokenFactory.tokenKey = configuration.getTokenBasedAuthConfiguration().getTokenKey();
        addExceptionMappers(environment);
    }

    public void run(String... args) throws Exception {
        super.run(args);


    }

    private void addExceptionMappers(Environment environment) {
        environment.jersey().register(new RobeExceptionMapper());

    }

}
