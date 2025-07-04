package com.leverx.learningmanagementsystem.btp.xsuaa.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("xsuaa")
@Data
@Profile("cloud")
public class XsuaaProperties {

    private String url;

    private String clientId;

    private String clientSecret;
}
