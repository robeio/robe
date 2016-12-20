package io.robe.websocket;

import com.google.inject.Injector;
import io.dropwizard.setup.Environment;
import io.robe.guice.scanner.Scanner;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class WebSocketScanner implements Scanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<?>> wsClasses = reflections.getTypesAnnotatedWith(RobeWebSocket.class);
        for (Class<?> ws : wsClasses) {
            RobeWebSocket ann = ws.getAnnotation(RobeWebSocket.class);
            String path = ann.path() + (ann.subPaths() ? "/*" : "");
            path = path.charAt(0) != '/' ? path = "/" + path : path;
            environment.servlets().addServlet(ann.path(), new WebSocketServlet((Class<WebSocket>) ws)).addMapping(path);
            LOGGER.info("Added websocket: " + ws);
        }

    }
}
