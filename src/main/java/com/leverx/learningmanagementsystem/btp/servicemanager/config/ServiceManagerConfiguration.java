package com.leverx.learningmanagementsystem.btp.servicemanager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("service-manager")
@Data
public class ServiceManagerConfiguration {

    private String url;

    private String smUrl;

    private String clientId;

    private String clientSecret;
}
