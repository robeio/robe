package io.robe.servlet;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import io.dropwizard.setup.Environment;
import io.robe.guice.scanner.Scanner;
import org.eclipse.jetty.servlet.ServletHolder;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ServletScanner implements Scanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServletScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<?>> servletClasses = reflections.getTypesAnnotatedWith(RobeServlet.class);
        for (Class<?> servlet : servletClasses) {
            RobeServlet ann = servlet.getAnnotation(RobeServlet.class);
            ServletHolder holder = new ServletHolder();
            try {
                holder.setServlet(new WrapperServlet((Class<ResourceServlet>) servlet, ann.singleton()));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            environment.getApplicationContext().addServlet(holder, ann.path());
            LOGGER.info("Added servlet: " + servlet);
        }

        createServletEventListener(environment, injector);
    }

    private void createServletEventListener(Environment environment, final Injector injector) {
        environment.getApplicationContext().addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return injector;
            }
        });
    }
}
