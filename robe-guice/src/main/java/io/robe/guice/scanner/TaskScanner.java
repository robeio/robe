package io.robe.guice.scanner;

import com.google.inject.Injector;
import io.dropwizard.servlets.tasks.Task;
import io.dropwizard.setup.Environment;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Collects all classes extended {@link io.dropwizard.servlets.tasks.Task} and adds them to admin servlet
 */
public class TaskScanner  implements Scanner{
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskScanner.class);

    @Override
    public void scanAndAdd(Environment environment, Injector injector, Reflections reflections) {
        Set<Class<? extends Task>> taskClasses = reflections.getSubTypesOf(Task.class);
        for (Class<? extends Task> task : taskClasses) {
            environment.admin().addTask(injector.getInstance(task));
            LOGGER.info("Added task: " + task);
        }
    }
}
