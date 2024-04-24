package com.pryvat.bank.task.manager.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "datasource-postgres")
@Getter
@Setter
public class PostgresDatabaseConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
