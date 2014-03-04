package io.robe.common.cli;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.cli.EnvironmentCommand;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.ServerFactory;
import com.yammer.dropwizard.lifecycle.ServerLifecycleListener;
import net.sourceforge.argparse4j.inf.Namespace;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ShutdownHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ControllableServerCommand<T extends Configuration> extends EnvironmentCommand<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControllableServerCommand.class);

    private final Class<T> configurationClass;


    public ControllableServerCommand(Service<T> service) {
        super(service, "cserver", "Runs the Dropwizard service as an HTTP server");
        this.configurationClass = service.getConfigurationClass();
    }
    protected Class<T> getConfigurationClass() {
        return configurationClass;
    }


    private void addShutdownHandler(Server server) {
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(server.getHandlers());
        handlers.addHandler(new ShutdownHandler(server, "stopJetty"));
        server.setHandler(handlers);
        LOGGER.info("Added Shutdown Handlers");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        final Server server = new ServerFactory(configuration.getHttpConfiguration(), environment.getName()).buildServer(environment);

        logBanner(environment.getName(), LOGGER);
        try {
            addShutdownHandler(server);
            server.start();
            for (ServerLifecycleListener listener : environment.getServerListeners()) {
                listener.serverStarted(server);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to start server, shutting down", e);
            server.stop();
        }
    }

    private void logBanner(String name, Logger logger) {
        try {
            final String banner = Resources.toString(Resources.getResource("banner.txt"), Charsets.UTF_8);
            logger.info("Starting {}\n{}", name, banner);
        } catch (IllegalArgumentException ignored) {
            // don't display the banner if there isn't one
            logger.info("Starting {}", name);
        } catch (IOException ignored) {
            logger.info("Starting {}", name);
        }
    }
}
