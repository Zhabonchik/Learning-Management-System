package com.leverx.learningmanagementsystem.btp.featureflagservice.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("feature-flags")
@Data
@Profile("cloud")
public class FeatureFlagsProperties {

    private String uri;
    private String username;
    private String password;
}
