package com.epam.kamisarau.auth.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Data
@PropertySource(value = "classpath:jdbc.properties")
@Configuration
public class JdbcDataSourceConfig {
    @Value("classpath:generate-tables.sql")
    private Resource createTablesSchema;
    @Value("classpath:create-trigger.sql")
    private Resource createTriggerSchema;

    private static final String BASIC_DELIMITER = ";";
    private static final String CUSTOM_DELIMITER_FOR_TRIGGER = "&&&";

    @Value("${jdbc.mysql.driverClass}")
    private String driverClass;
    @Value("${jdbc.mysql.url}")
    private String url;
    @Value("${jdbc.mysql.username}")
    private String username;
    @Value("${jdbc.mysql.password}")
    private String password;

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        DatabasePopulatorUtils.execute(createDatabasePopulator(createTablesSchema, BASIC_DELIMITER), dataSource);
        DatabasePopulatorUtils.execute(createDatabasePopulator(createTriggerSchema, CUSTOM_DELIMITER_FOR_TRIGGER), dataSource);
        return dataSource;
    }

    private DatabasePopulator createDatabasePopulator(Resource schemaScript, String delimiter) {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        if (!delimiter.isEmpty()) {
            databasePopulator.setSeparator(delimiter);
        }
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(schemaScript);
        return databasePopulator;
    }
}
