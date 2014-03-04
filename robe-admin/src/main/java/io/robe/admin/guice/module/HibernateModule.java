package io.robe.admin.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import io.robe.hibernate.HibernateBundle;
import org.hibernate.SessionFactory;

/**
 * Default Guice bindings are done at this class.
 */
public class HibernateModule extends AbstractModule {

    private final HibernateBundle bundle;

    public HibernateModule(HibernateBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    protected void configure() {

        bind(HibernateBundle.class).toProvider(new Provider<HibernateBundle>() {
            @Override
            public HibernateBundle get() {
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
