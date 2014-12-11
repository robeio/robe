package io.robe.guice.scanner;


import com.google.inject.Injector;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;

/**
 * Scanner is a interface for GuiceBundle to scan and add custom classes on external modules and projects.
 */
public interface Scanner {


    /**
     * This method will be called once the GuiceBundle starts.
     * @param environment Dropwizard environment
     * @param injector  Guice Injector
     * @param reflections GuiceBundle reflections collected according to configuration scan paths
     */
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections);
}
