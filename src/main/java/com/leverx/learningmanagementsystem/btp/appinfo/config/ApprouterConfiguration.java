package com.leverx.learningmanagementsystem.btp.appinfo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApprouterConfiguration {

    @Value("${APPROUTER_NAME}")
    private String name;
}
