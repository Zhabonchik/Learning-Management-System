package com.leverx.learningmanagementsystem.btp.destinationservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("destination")
@Data
@Profile("cloud")
public class DestinationServiceConfiguration {

    private String uri;

    private String url;

    private String clientId;

    private String clientSecret;
}
