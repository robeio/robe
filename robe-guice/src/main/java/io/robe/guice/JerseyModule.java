package io.robe.guice;

import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.BootstrapModule;
import com.squarespace.jersey2.guice.BootstrapUtils;
import org.glassfish.hk2.api.ServiceLocator;

//Copied from https://github.com/HubSpot/dropwizard-guice/blob/master/src/main/java/com/hubspot/dropwizard/guice/JerseyModule.java
public class JerseyModule extends ServletModule {

    @Override
    protected void configureServlets() {
        // The order these operations (including the steps in the linker) are important
        ServiceLocator locator = new ServiceLocatorDecorator(BootstrapUtils.newServiceLocator()) {

            @Override
            public void shutdown() {
                // don't shutdown, see issue #67. Remove once jersey2-guice supports Jersey 2.21
            }
        };
        install(new BootstrapModule(locator));

        bind(HK2Linker.class).asEagerSingleton();
    }
}