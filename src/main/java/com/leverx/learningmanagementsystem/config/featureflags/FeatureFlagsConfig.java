package com.leverx.learningmanagementsystem.config.featureflags;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("feature-flags")
@Data
@Profile("hana")
public class FeatureFlagsConfig {
    private String uri;
    private String username;
    private String password;
}
