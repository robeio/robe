package io.robe.hibernate;

import io.dropwizard.hibernate.SessionFactoryFactory;
import io.robe.hibernate.conf.RobeHibernateNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobeSessionFactoryFactory extends SessionFactoryFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(RobeSessionFactoryFactory.class);

    @Override
    protected void configure(Configuration configuration, ServiceRegistry registry) {
        determineNamingStrategy(configuration);
    }

    private void determineNamingStrategy(Configuration configuration) {
        String prefix = configuration.getProperty("hibernate.prefix");
        if (prefix != null) {
            configuration.setNamingStrategy(new RobeHibernateNamingStrategy(prefix));
            LOGGER.info("Table Prefix: ", prefix);
        }

        String namingStrategy = configuration.getProperty("hibernate.namingStrategy");
        if (namingStrategy != null) {
            LOGGER.warn("Hibernate.prefix property will be ignored, cause: another type of naming strategy selected");
            try {
                configuration.setNamingStrategy((NamingStrategy) Class.forName(namingStrategy).newInstance());
            } catch (Exception e) {
                LOGGER.error("Can't set Hibernate Naming Strategy", e);
            }
        }

    }

}
