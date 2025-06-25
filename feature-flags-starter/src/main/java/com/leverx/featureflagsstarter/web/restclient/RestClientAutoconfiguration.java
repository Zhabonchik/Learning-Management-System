package com.leverx.featureflagsstarter.web.restclient;

import com.leverx.featureflagsstarter.config.FeatureFlagsServiceAutoconfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@AutoConfigureBefore(FeatureFlagsServiceAutoconfiguration.class)
public class RestClientAutoconfiguration {

    @Bean
    @ConditionalOnMissingBean(RestTemplateBuilder.class)
    public RestTemplateBuilder restTemplateBuilder(){
        return new RestTemplateBuilder();
    }

    @Bean
    @ConditionalOnMissingBean(RestClient.class)
    public RestClient restClient(RestTemplateBuilder builder){
        return RestClient.builder()
                .requestFactory(builder.build().getRequestFactory())
                .build();
    }
}
