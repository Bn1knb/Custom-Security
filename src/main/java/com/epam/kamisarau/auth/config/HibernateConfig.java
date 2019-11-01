package com.epam.kamisarau.auth.config;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:hb.properties")
public class HibernateConfig {
    @Value("${mysql.show.sql}")
    private String showSql;
    @Value("${mysql.current.session.context}")
    private String contextClass;

    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactoryBean() {

        LocalEntityManagerFactoryBean localEntityManagerFactoryBean = new LocalEntityManagerFactoryBean();
        localEntityManagerFactoryBean.setPersistenceUnitName("persistence");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        localEntityManagerFactoryBean.setJpaProperties(getProperties());

        return localEntityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager jpaTransMan(){
        return new JpaTransactionManager(
                entityManagerFactoryBean().getObject());
    }

    private Properties getProperties() {
        Properties settings = new Properties();
        settings.put(Environment.SHOW_SQL, showSql);
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, contextClass);

        return settings;
    }
}
