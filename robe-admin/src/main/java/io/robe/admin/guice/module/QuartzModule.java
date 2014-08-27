package io.robe.admin.guice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import io.robe.quartz.ManagedQuartz;
import io.robe.quartz.QuartzBundle;

/**
 * Default Guice bindings are done at this class.
 */
public class QuartzModule extends AbstractModule {

    private final QuartzBundle bundle;

    public QuartzModule(QuartzBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    protected void configure() {
        bind(ManagedQuartz.class).toProvider(new Provider<ManagedQuartz>() {
            @Override
            public ManagedQuartz get() {
                return new ManagedQuartz(bundle.getScheduler(), bundle.getOnStartJobs(), bundle.getOnStopJobs());
            }
        });
    }


}

