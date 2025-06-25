package com.leverx.featureflagsstarter.btp.featureflagservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("feature-flags")
@Data
public class FeatureFlagsConfiguration {

    private String uri;
    private String username;
    private String password;
}
