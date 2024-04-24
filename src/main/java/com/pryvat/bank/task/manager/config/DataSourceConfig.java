package com.pryvat.bank.task.manager.config;

import com.pryvat.bank.task.manager.router.DataSourceRouter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {
    private final H2DatabaseConfig h2DatabaseConfig;
    private final PostgresDatabaseConfig postgresDatabaseConfig;
    @Bean
    public DataSource dataSource() {
        DataSourceRouter router = new DataSourceRouter();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("h2", h2DataSource());
        targetDataSources.put("postgresql", postgresqlDataSource());

        router.setTargetDataSources(targetDataSources);
        router.setDefaultTargetDataSource(h2DataSource());

        return router;
    }

    public DataSource h2DataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(h2DatabaseConfig.getDriverClassName());
        dataSource.setUrl(h2DatabaseConfig.getUrl());
        dataSource.setUsername(h2DatabaseConfig.getUsername());
        dataSource.setPassword(h2DatabaseConfig.getPassword());

        return dataSource;
    }

    public DataSource postgresqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgresDatabaseConfig.getDriverClassName());
        dataSource.setUrl(postgresDatabaseConfig.getUrl());
        dataSource.setUsername(postgresDatabaseConfig.getUsername());
        dataSource.setPassword(postgresDatabaseConfig.getPassword());
        dataSource.setSchema("public");

        return dataSource;
    }
}
