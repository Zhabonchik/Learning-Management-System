package com.leverx.learningmanagementsystem.btp.appinfo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ConfigurationProperties("xsuaa")
@Data
@Profile("cloud")
public class ApplicationInfoConfiguration {

    private String url;

    private String clientId;

    private String clientSecret;
}
