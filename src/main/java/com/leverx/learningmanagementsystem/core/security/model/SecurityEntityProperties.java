package com.leverx.learningmanagementsystem.core.security.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security-entity")
@Data
public class SecurityEntityProperties {

    @NestedConfigurationProperty
    private SecurityEntity user;

    @NestedConfigurationProperty
    private SecurityEntity manager;

    @NestedConfigurationProperty
    private SecurityEntity admin;
}
