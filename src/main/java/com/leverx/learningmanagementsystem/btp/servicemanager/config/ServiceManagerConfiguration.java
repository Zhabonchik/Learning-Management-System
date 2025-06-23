package com.leverx.learningmanagementsystem.btp.servicemanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("service-manager")
@Data
@Profile("cloud")
public class ServiceManagerConfiguration {

    private String tokenUrl;

    private String url;

    private String clientId;

    private String clientSecret;
}
