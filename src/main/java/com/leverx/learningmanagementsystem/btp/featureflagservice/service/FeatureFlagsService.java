package com.leverx.learningmanagementsystem.btp.featureflagservice.service;

import com.leverx.learningmanagementsystem.btp.featureflagservice.config.FeatureFlagsConfiguration;
import com.leverx.learningmanagementsystem.btp.featureflagservice.model.FeatureFlagsResponse;
import com.leverx.learningmanagementsystem.core.exception.model.FeatureFlagsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

import static com.leverx.learningmanagementsystem.btp.featureflagservice.constants.FeatureFlagsServiceConstants.API;
import static com.leverx.learningmanagementsystem.btp.featureflagservice.constants.FeatureFlagsServiceConstants.EVALUATE;
import static com.leverx.learningmanagementsystem.btp.featureflagservice.constants.FeatureFlagsServiceConstants.V2;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class FeatureFlagsService {

    private final FeatureFlagsConfiguration featureFlagsConfiguration;
    private final RestClient restClient;

    public boolean getFlag(String flag) {
        String url = getUrl(flag);
        HttpHeaders headers = configureHttpHeaders();

        log.info("Get flag {}", flag);
        FeatureFlagsResponse response = restClient.get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .body(FeatureFlagsResponse.class);

        if (isNull(response)) {
            throw new FeatureFlagsException("Feature flag " + flag + " not found");
        }

        validateResponse(response);

        log.info("Flag {} is {}", flag, response.variation());
        return response.variation();
    }

    private HttpHeaders configureHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "%s:%s".formatted(featureFlagsConfiguration.getUsername(), featureFlagsConfiguration.getPassword());
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(UTF_8));

        headers.set("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    private String getUrl(String flag) {
        return UriComponentsBuilder.fromUriString(featureFlagsConfiguration.getUri())
                .pathSegment(API, V2, EVALUATE, flag)
                .toUriString();
    }

    private void validateResponse(FeatureFlagsResponse response) {
        if (isNull(response.httpStatus()) || response.httpStatus() != HttpStatus.OK.value()) {
            log.info("Feature flag validation failed");
            throw new FeatureFlagsException("Could not get feature flag response. Request status: " + response.httpStatus());
        }
    }
}
