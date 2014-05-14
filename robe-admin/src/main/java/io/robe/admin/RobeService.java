package io.robe.admin;

import com.google.inject.Module;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.hibernate.UnitOfWork;
import com.yammer.dropwizard.views.ViewBundle;
import io.robe.admin.cli.InitializeCommand;
import io.robe.admin.guice.module.AuthenticatorModule;
import io.robe.admin.guice.module.HibernateModule;
import io.robe.admin.guice.module.MailModule;
import io.robe.admin.guice.module.QuartzModule;
import io.robe.auth.tokenbased.TokenBasedAuthBundle;
import io.robe.common.cli.ControllableServerCommand;
import io.robe.common.exception.RobeExceptionMapper;
import io.robe.guice.GuiceBundle;
import io.robe.hibernate.HibernateBundle;
import io.robe.mail.MailBundle;
import io.robe.quartz.QuartzBundle;
import io.robe.quartz.hibernate.ByHibernate;

import javax.ws.rs.ext.ExceptionMapper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Default io.robe.admin class of Robe.
 * If you extend this class on your applications io.robe.admin class and call super methods at
 * overridden methods you will still benefit of robe souse.
 */
public class RobeService extends Service<RobeServiceConfiguration> {


    public static void main(String[] args) throws Exception {
        new RobeService().run(args);
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
    public void initialize(Bootstrap<RobeServiceConfiguration> bootstrap) {
        bootstrap.addCommand(new ControllableServerCommand<RobeServiceConfiguration>(this));
        HibernateBundle<RobeServiceConfiguration> hibernateBundle = new HibernateBundle<RobeServiceConfiguration>();
        QuartzBundle<RobeServiceConfiguration> quartzBundle = new QuartzBundle<RobeServiceConfiguration>();
        MailBundle<RobeServiceConfiguration> mailBundle = new MailBundle<RobeServiceConfiguration>();
        TokenBasedAuthBundle<RobeServiceConfiguration> authBundle = new TokenBasedAuthBundle<RobeServiceConfiguration>();

        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(authBundle);
        bootstrap.addBundle(quartzBundle);
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(mailBundle);
        bootstrap.addBundle(new NamedAssetsBundle("/admin-ui/", "/admin-ui", "admin-ui/index.html", "io/robe/admin"));


        List<Module> modules = new LinkedList<Module>();
        modules.add(new HibernateModule(hibernateBundle));
        modules.add(new AuthenticatorModule(authBundle));
        modules.add(new QuartzModule(quartzBundle));
        modules.add(new MailModule(mailBundle));

        bootstrap.addBundle(new GuiceBundle<RobeServiceConfiguration>(modules));
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
    public void run(RobeServiceConfiguration configuration, Environment environment) throws Exception {
        addExceptionMappers(environment);
        environment.start();
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
