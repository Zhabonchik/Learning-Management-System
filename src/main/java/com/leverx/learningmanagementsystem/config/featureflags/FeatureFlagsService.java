package com.leverx.learningmanagementsystem.config.featureflags;

import com.leverx.learningmanagementsystem.utils.exception.model.FeatureFlagsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.API;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.EVALUATE;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.V2;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static org.springframework.http.HttpMethod.GET;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class FeatureFlagsService {

    private final FeatureFlagsConfiguration featureFlagsConfiguration;
    private final RestTemplate restTemplate;

    public boolean getFlag(String flag) {
        String url = getUrl(flag);
        HttpEntity<Void> entity = configureHttpEntity();

        log.info("Get flag {}", flag);
        FeatureFlagsResponse response = restTemplate.exchange(
                url,
                GET,
                entity,
                FeatureFlagsResponse.class
        ).getBody();

        if (isNull(response)) {
            throw new FeatureFlagsException("Feature flag " + flag + " not found");
        }

        validateResponse(response);

        log.info("Flag {} is {}", flag, response.variation());
        return response.variation();
    }

    private HttpEntity<Void> configureHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        String auth = "%s:%s".formatted(featureFlagsConfiguration.getUsername(), featureFlagsConfiguration.getPassword());
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(UTF_8));

        headers.set("Authorization", "Basic " + encodedAuth);
        return new HttpEntity<>(headers);
    }

    private String getUrl(String flag) {
        return UriComponentsBuilder.fromUriString(featureFlagsConfiguration.getUri())
                .pathSegment(API, V2, EVALUATE, flag)
                .toUriString();
    }

    private void validateResponse(FeatureFlagsResponse response) {
        if (isNull(response.httpStatus()) || response.httpStatus() != 200) {
            log.info("Feature flag validation failed");
            throw new FeatureFlagsException("Could not get feature flag response. Request status: " + response.httpStatus());
        }
    }
}
