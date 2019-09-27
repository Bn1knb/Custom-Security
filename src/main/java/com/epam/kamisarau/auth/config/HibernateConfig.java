package com.epam.kamisarau.auth.config;

import com.epam.kamisarau.auth.model.RoleModel;
import com.epam.kamisarau.auth.model.TokenModel;
import com.epam.kamisarau.auth.model.UserModel;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@PropertySource(value = "classpath:hb.properties")
public class HibernateConfig {
    @Value("${mysql.driver}")
    private String driver;
    @Value("${mysql.url}")
    private String url;
    @Value("${mysql.user}")
    private String user;
    @Value("${mysql.password}")
    private String password;
    @Value("${mysql.dialect}")
    private String dialect;
    @Value("${mysql.show.sql}")
    private String showSql;
    @Value("${mysql.ddl-auto}")
    private String ddlAuto;

    @Bean(name = "HbSessionFactory")
    SessionFactory getSessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperties(getProperties());
        configuration.addAnnotatedClass(RoleModel.class);
        configuration.addAnnotatedClass(UserModel.class);
        configuration.addAnnotatedClass(TokenModel.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    private Properties getProperties() {
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, driver);
        settings.put(Environment.URL, url);
        settings.put(Environment.USER, user);
        settings.put(Environment.PASS, password);
        settings.put(Environment.DIALECT, dialect);
        settings.put(Environment.SHOW_SQL, showSql);
        settings.put(Environment.HBM2DDL_AUTO, ddlAuto);

        return settings;
    }
}
