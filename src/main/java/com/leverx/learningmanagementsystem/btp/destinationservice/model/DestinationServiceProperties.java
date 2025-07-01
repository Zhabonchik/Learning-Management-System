package com.leverx.learningmanagementsystem.btp.destinationservice.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("destination")
@Data
@Profile("cloud")
public class DestinationServiceProperties {

    private String uri;

    private String tokenUrl;

    private String clientId;

    private String clientSecret;

    private String xsappname;
}
