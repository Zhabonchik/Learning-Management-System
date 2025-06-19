package com.leverx.learningmanagementsystem.multitenancy.connection.datasource.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.datasource")
@Data
public class DataSourceConfiguration {

    private String url;

    private String username;

    private String password;

    private String driverClassName;

    @Value("${db.changelog}")
    private String dbChangelog;
}
