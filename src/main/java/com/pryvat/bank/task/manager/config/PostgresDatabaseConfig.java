package com.pryvat.bank.task.manager.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "postgreEntityManagerFactory",
        transactionManagerRef = "postgreTransactionManager",
        basePackages = { "com.pryvat.bank.task.manager.repository.postgres" }
)
public class PostgresDatabaseConfig {
    @Value("${datasource-postgres.dialect}")
    private String hibernateDialect;
    @Value("${datasource-postgres.hibernate.hbm2ddl.auto}")
    private String ddl;
    @Bean(name = "postgreDataSource")
    @ConfigurationProperties(prefix = "datasource-postgres")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "postgreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean
    entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("postgreDataSource") DataSource dataSource
    ) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("hibernate.dialect", hibernateDialect);
        props.put("hibernate.hbm2ddl.auto", ddl);
        return builder
                .dataSource(dataSource)
                .packages("com.pryvat.bank.task.manager.entity.*")
                .persistenceUnit("postgre")
                .properties(props)
                .build();
    }

    @Bean(name = "postgreTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("postgreEntityManagerFactory") EntityManagerFactory
                    entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
