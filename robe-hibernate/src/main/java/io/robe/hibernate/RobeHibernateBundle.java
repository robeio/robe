package io.robe.hibernate;

import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.google.common.collect.ImmutableList;
import io.dropwizard.Configuration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.robe.hibernate.entity.BaseEntity;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;


/**
 * Hibernate bundle with Entity classpath scanner.
 * Takes scannable paths from configuration yml from entityPackage element as array separated by ','
 */
public class RobeHibernateBundle<T extends Configuration & HasHibernateConfiguration> extends HibernateBundle<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobeHibernateBundle.class);

    private static RobeHibernateBundle instance;

    private RobeHibernateBundle(ImmutableList<Class<?>> entities, SessionFactoryFactory sessionFactoryFactory) {
        super(entities, sessionFactoryFactory);
    }

    public static final RobeHibernateBundle createInstance(String[] packages, String[] entities) {
        if (instance == null)
            instance = new RobeHibernateBundle(loadEntities(packages, entities), new RobeSessionFactoryFactory());
        else
            throw new RuntimeException("HibernateBundle is already created.");
        return instance;
    }

    public static final RobeHibernateBundle getInstance() {
        if (instance != null)
            return instance;
        else
            throw new RuntimeException("HibernateBundle is not created. Please call createInstance first.");
    }

    private static final ImmutableList<Class<?>> loadEntities(String[] packages, String[] entities) {
        Set<Class<?>> classes = new HashSet<>();
        if (packages != null) {
            for (String packageName : packages) {
                LOGGER.info("Loading Package: " + packageName);
                Reflections reflections = new Reflections(packageName);
                classes.addAll(reflections.getTypesAnnotatedWith(Entity.class));
            }
        }
        if (entities != null) {
            for (String entity : entities) {
                try {
                    LOGGER.info("Loading Entity: " + entity);
                    Class entityClass = Class.forName(entity);
                    classes.add(BaseEntity.class);
                    if (entityClass.isAnnotationPresent(Entity.class)) {
                        classes.add(entityClass);
                    } else {
                        LOGGER.warn("Class is not annotated with Entity: " + entity);
                    }
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("Can't load class: " + entity, e);
                }
            }
        }
        return ImmutableList.<Class<?>>builder().add(BaseEntity.class).addAll(classes).build();
    }

    /**
     * read more https://github.com/dropwizard/dropwizard/issues/932
     *
     * @return Hibernate4Module
     */

    @Override
    protected Hibernate4Module createHibernate4Module() {
        Hibernate4Module module = new Hibernate4Module();
        module.disable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);
        return module;
    }

    public HibernateConfiguration getDatabaseConfiguration(T configuration) {
        return configuration.getHibernateConfiguration();
    }

    @Override
    public PooledDataSourceFactory getDataSourceFactory(T configuration) {
        return configuration.getHibernateConfiguration().getDataSourceFactory(configuration);
    }


    protected void configure(org.hibernate.cfg.Configuration configuration) {

    }

}


