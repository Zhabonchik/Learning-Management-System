package com.leverx.learningmanagementsystem.config.destination;

import com.leverx.learningmanagementsystem.email.DestinationServiceMailConfig;
import com.leverx.learningmanagementsystem.email.DestinationServiceResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
@Profile("hana")
public class DestinationService {

    private final DestinationConfig destinationConfig;
    private final RestTemplateBuilder restTemplateBuilder;

    @Retryable(
            retryFor = { HttpClientErrorException.class, ResourceAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public DestinationServiceMailConfig getEmailConfig(String destinationName) {
        String uri = getUri(destinationName);
        String authToken = getAuthToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        log.info("Getting email config from {}", uri);
        ResponseEntity<DestinationServiceResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, entity, DestinationServiceResponse.class);

        return Objects.requireNonNull(response.getBody()).getDestinationConfiguration();
    }

    @Cacheable("authTokens")
    @Retryable(
            retryFor = { HttpClientErrorException.class, ResourceAccessException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public String getAuthToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String authUrl = getAuthUrl();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = configureAuthenticationRestTemplate();

        log.info("Get auth token");
        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);

        return Objects.requireNonNull(response.getBody())
                .get("access_token")
                .toString()
                .trim()
                .replaceAll("[\\n\\r]", "");
    }

    private String getUri(String destinationName) {
        return UriComponentsBuilder.fromUriString(destinationConfig.getUri())
                .pathSegment("destination-configuration", "v1", "destinations", destinationName)
                .toUriString();
    }

    private String getAuthUrl() {
        return UriComponentsBuilder.fromUriString(destinationConfig.getUrl())
                .pathSegment("oauth", "token")
                .toUriString();
    }

    private RestTemplate configureAuthenticationRestTemplate() {
        return restTemplateBuilder
                .basicAuthentication(destinationConfig.getClientId(), destinationConfig.getClientSecret())
                .build();
    }

}
