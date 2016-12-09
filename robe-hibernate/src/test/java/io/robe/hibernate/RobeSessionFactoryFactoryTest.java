package io.robe.hibernate;

import org.hibernate.cfg.Configuration;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by adem on 26/10/2016.
 */
public class RobeSessionFactoryFactoryTest {

    @Test
    public void configure() {
        Configuration configuration = mock(Configuration.class);
        RobeSessionFactoryFactory factory = new RobeSessionFactoryFactory();
        when(configuration.getProperty("hibernate.prefix")).thenReturn("P_");
        factory.configure(configuration, null);
        when(configuration.getProperty("hibernate.prefix")).thenReturn(null);
        factory.configure(configuration, null);
    }

}
