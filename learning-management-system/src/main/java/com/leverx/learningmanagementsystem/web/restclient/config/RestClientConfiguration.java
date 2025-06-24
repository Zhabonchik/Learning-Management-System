package com.leverx.learningmanagementsystem.web.restclient.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient restClient(RestTemplateBuilder builder){
        return RestClient.builder()
                .requestFactory(builder.build().getRequestFactory())
                .build();
    }
}
