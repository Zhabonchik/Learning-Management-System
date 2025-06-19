package com.leverx.learningmanagementsystem.btp.appinfo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Data
@Profile("cloud")
public class ApprouterConfiguration {

    @Value("${APPROUTER_NAME}")
    private String name;
}
