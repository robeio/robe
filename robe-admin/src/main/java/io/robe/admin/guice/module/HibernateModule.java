package io.robe.admin.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import io.robe.hibernate.RobeHibernateBundle;
import org.hibernate.SessionFactory;

/**
 * Default Guice bindings are done at this class.
 */
public class HibernateModule extends AbstractModule {

    private final RobeHibernateBundle bundle;

    public HibernateModule(RobeHibernateBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    protected void configure() {

        bind(RobeHibernateBundle.class).toProvider(new Provider<RobeHibernateBundle>() {
            @Override
            public RobeHibernateBundle get() {
                return bundle;
            }
        });

        bind(SessionFactory.class).toProvider(new Provider<SessionFactory>() {
            @Override
            public SessionFactory get() {
                return bundle.getSessionFactory();
            }
        });
    }


}
