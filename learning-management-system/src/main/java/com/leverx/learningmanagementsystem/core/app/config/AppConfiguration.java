package com.leverx.learningmanagementsystem.core.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("application")
@Data
@Profile("cloud")
public class AppConfiguration {

    private String applicationId;

    private String spaceId;

    private String uri;
}
