package com.leverx.learningmanagementsystem.btp.xsuaa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("xsuaa")
@Data
@Profile("cloud")
public class XsuaaConfiguration {

    private String url;

    private String clientId;

    private String clientSecret;
}
