package com.leverx.learningmanagementsystem.config.featureflags;

import com.leverx.learningmanagementsystem.utils.exception.model.FeatureFlagsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
@Profile("hana")
public class FeatureFlagsService {

    private final FeatureFlagsConfig featureFlagsConfig;
    private final RestTemplateBuilder restTemplateBuilder;

    public boolean getFlag(String flagName) {
        RestTemplate restTemplate = getRestTemplate(flagName);
        String url = getUrl(flagName);

        log.info("Get flag {}", flagName);
        FeatureFlagsResponse response = restTemplate.getForObject(url, FeatureFlagsResponse.class);
        if (Objects.isNull(response)) {
            throw new FeatureFlagsException("Feature flag '" + flagName + "' not found");
        }

        validateResponse(response);

        log.info("Flag {} is {}", flagName, response.variation());
        return response.variation();
    }

    private RestTemplate getRestTemplate(String flagName) {
        log.info("Get rest template for username: {}", featureFlagsConfig.getUsername());
        return restTemplateBuilder
                .basicAuthentication(featureFlagsConfig.getUsername(), featureFlagsConfig.getPassword()).build();
    }

    private String getUrl(String flagName) {
        return UriComponentsBuilder.fromUriString(featureFlagsConfig.getUri())
                .pathSegment("api", "v2", "evaluate", flagName)
                .toUriString();
    }

    private void validateResponse(FeatureFlagsResponse response) {
        if (Objects.isNull(response.httpStatus()) || response.httpStatus() != 200) {
            log.info("Feature flag validation failed");
            throw new FeatureFlagsException("Could not get feature flag response. Request status: " + response.httpStatus());
        }
    }
}
