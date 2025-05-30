package com.leverx.learningmanagementsystem.web.security.config;

import com.leverx.learningmanagementsystem.web.security.model.SecurityEntity;
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
