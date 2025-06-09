package com.leverx.learningmanagementsystem.multitenancy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("spring.datasource")
@Profile({"local", "cloud"})
@Data
public class DataSourceConfiguration {

    private String url;

    private String username;

    private String password;

    private String driverClassName;
}
