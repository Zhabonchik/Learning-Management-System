package com.leverx.learningmanagementsystem.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("security-entity")
@Data
public class SecurityEntityConfiguration {
    @NestedConfigurationProperty
    private SecurityEntity user;
    @NestedConfigurationProperty
    private SecurityEntity manager;
}
