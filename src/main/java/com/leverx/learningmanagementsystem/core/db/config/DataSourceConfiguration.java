package com.leverx.learningmanagementsystem.core.db.config;

import lombok.Data;
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
}
