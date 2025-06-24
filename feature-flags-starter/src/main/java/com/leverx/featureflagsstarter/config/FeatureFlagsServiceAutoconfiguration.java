package com.leverx.featureflagsstarter.config;

import com.leverx.featureflagsstarter.btp.featureflagservice.config.FeatureFlagsConfiguration;
import com.leverx.featureflagsstarter.btp.featureflagservice.service.FeatureFlagsService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@AutoConfiguration
@ConditionalOnClass(FeatureFlagsService.class)
@ConditionalOnProperty(prefix = "feature-flags", name = "enabled", havingValue = "true", matchIfMissing = false)
public class FeatureFlagsServiceAutoconfiguration {

    @Bean
    @ConditionalOnProperties({
            @ConditionalOnProperty(prefix = "feature-flags", name = "uri"),
            @ConditionalOnProperty(prefix = "feature-flags", name = "username"),
            @ConditionalOnProperty(prefix = "feature-flags", name = "password")
    })
    @ConditionalOnMissingBean(FeatureFlagsConfiguration.class)
    public FeatureFlagsConfiguration featureFlagsConfiguration() {
        return new FeatureFlagsConfiguration();
    }

    @Bean
    @ConditionalOnBean(FeatureFlagsConfiguration.class)
    @ConditionalOnMissingBean(FeatureFlagsService.class)
    public FeatureFlagsService featureFlagsService(FeatureFlagsConfiguration featureFlagsConfiguration, RestClient restClient) {
        return new FeatureFlagsService(featureFlagsConfiguration, restClient);
    }
}
