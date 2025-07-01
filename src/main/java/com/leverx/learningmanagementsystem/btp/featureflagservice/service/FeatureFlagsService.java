package com.leverx.learningmanagementsystem.btp.featureflagservice.service;

import com.leverx.learningmanagementsystem.btp.featureflagservice.model.FeatureFlagsProperties;
import com.leverx.learningmanagementsystem.btp.featureflagservice.model.FeatureFlags;
import com.leverx.learningmanagementsystem.core.exception.model.FeatureFlagsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import static com.leverx.learningmanagementsystem.btp.featureflagservice.constants.FeatureFlagsServiceConstants.API;
import static com.leverx.learningmanagementsystem.btp.featureflagservice.constants.FeatureFlagsServiceConstants.EVALUATE;
import static com.leverx.learningmanagementsystem.btp.featureflagservice.constants.FeatureFlagsServiceConstants.V2;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class FeatureFlagsService {

    private final FeatureFlagsProperties featureFlagsProperties;
    private final RestClient restClient;

    public boolean getFlag(String flag) {
        String url = getUrl(flag);
        var username = featureFlagsProperties.getUsername();
        var password = featureFlagsProperties.getPassword();

        log.info("Get flag {}", flag);
        FeatureFlags response = restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(username, password))
                .retrieve()
                .body(FeatureFlags.class);

        validateResponse(response, flag);

        log.info("Flag {} is {}", flag, response.variation());
        return response.variation();
    }

    private String getUrl(String flag) {
        return UriComponentsBuilder.fromUriString(featureFlagsProperties.getUri())
                .pathSegment(API, V2, EVALUATE, flag)
                .toUriString();
    }

    private void validateResponse(FeatureFlags response, String flag) {

        if (isNull(response)) {
            throw new FeatureFlagsException("Feature flag " + flag + " not found");
        }

        if (isNull(response.httpStatus()) || response.httpStatus() != HttpStatus.OK.value()) {
            log.info("Feature flag validation failed");
            throw new FeatureFlagsException("Could not get feature flag response. Request status: " + response.httpStatus());
        }
    }
}
