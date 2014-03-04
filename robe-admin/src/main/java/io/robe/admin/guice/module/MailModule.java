package io.robe.admin.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import io.robe.mail.MailBundle;
import io.robe.mail.MailSender;

public class MailModule extends AbstractModule {
    private final MailBundle bundle;


    public MailModule(MailBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Configures a {@link Binder} via the exposed methods.
     */
    @Override
    protected void configure() {
        bind(MailSender.class).toProvider(new Provider<MailSender>() {

            /**
             * Provides an instance of {@code T}. Must never return {@code null}.
             *
             * @throws OutOfScopeException when an attempt is made to access a scoped object while the scope
             *                             in question is not currently active
             * @throws ProvisionException  if an instance cannot be provided. Such exceptions include messages
             *                             and throwables to describe why provision failed.
             */
            @Override
            public MailSender get() {
                return bundle.getMailSender();
            }
        });
    }
}
