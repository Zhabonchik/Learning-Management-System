package com.leverx.learningmanagementsystem.config.destination;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("destination")
@Data
@Profile("cloud")
public class DestinationConfiguration {
    private String uri;
    private String url;
    private String clientId;
    private String clientSecret;
}
