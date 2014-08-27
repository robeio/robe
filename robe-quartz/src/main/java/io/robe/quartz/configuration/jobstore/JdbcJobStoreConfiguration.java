package io.robe.quartz.configuration.jobstore;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Properties;

public class JdbcJobStoreConfiguration extends JobStoreConfiguration {

    /**
     * For ex:
     * <p/>
     * org.quartz.jobStore.dataSource
     * org.quartz.dataSource.myDS.driver
     * org.quartz.dataSource.myDS.URL
     * org.quartz.dataSource.myDS.user
     * org.quartz.dataSource.myDS.password
     * org.quartz.dataSource.myDS.maxConnections
     * org.quartz.jobStore.tablePrefix
     * org.quartz.jobStore.driverDelegateClass
     */
    @JsonProperty
    @NotNull
    private Properties properties;

    @Override
    public String getClassName() {
        return "org.quartz.impl.jdbcjobstore.StdJDBCDelegate";
    }

}
