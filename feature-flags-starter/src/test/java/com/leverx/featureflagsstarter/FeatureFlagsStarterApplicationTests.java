package com.leverx.featureflagsstarter;

import com.leverx.featureflagsstarter.btp.featureflagservice.config.FeatureFlagsConfiguration;
import com.leverx.featureflagsstarter.btp.featureflagservice.service.FeatureFlagsService;
import com.leverx.featureflagsstarter.config.FeatureFlagsServiceAutoconfiguration;
import com.leverx.featureflagsstarter.web.restclient.RestClientAutoconfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

class FeatureFlagsStarterApplicationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(FeatureFlagsServiceAutoconfiguration.class, RestClientAutoconfiguration.class));

    @Test
    void whenApplicationPropertiesAreSet_thenBeansAreCreated() {
        contextRunner.withPropertyValues(
                "feature-flags.uri=https://feature-flags.cfapps.us10.hana.ondemand.com",
                "feature-flags.username=username",
                "feature-flags.password=password",
                "feature-flags.enabled=true"
        ).run(context -> {
            assertThat(context).hasSingleBean(FeatureFlagsService.class);
            assertThat(context).hasSingleBean(RestClient.class);
        });
    }

    @Test
    void whenRequiredPropertiesAreMissing_thenBeansAreNotCreated() {
        contextRunner
                .run(context -> {
                    assertThat(context).doesNotHaveBean(FeatureFlagsConfiguration.class);
                    assertThat(context).doesNotHaveBean(FeatureFlagsService.class);
                });
    }

}
