package io.robe.hibernate;

import io.dropwizard.hibernate.SessionFactoryFactory;
import io.robe.hibernate.conf.RobeHibernateNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RobeSessionFactoryFactory extends SessionFactoryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(RobeSessionFactoryFactory.class);

    @Override
    protected void configure(Configuration configuration, ServiceRegistry registry) {
        String prefix = configuration.getProperty("hibernate.prefix");
        if (prefix != null) {
            configuration.setPhysicalNamingStrategy(new RobeHibernateNamingStrategy(prefix));
            LOGGER.info("Table Prefix: ", prefix);
        }
    }

}
