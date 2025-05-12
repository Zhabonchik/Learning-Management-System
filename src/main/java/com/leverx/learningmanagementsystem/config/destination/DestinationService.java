package com.leverx.learningmanagementsystem.config.destination;

import com.leverx.learningmanagementsystem.email.DestinationServiceMailConfig;
import com.leverx.learningmanagementsystem.email.DestinationServiceResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
@Profile("hana")
public class DestinationService {

    private final DestinationConfig destinationConfig;
    private final RestTemplateBuilder restTemplateBuilder;

    public DestinationServiceMailConfig getEmailConfig(String destinationName) {
        String uri = getUri(destinationName);
        String authToken = getAuthToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            log.info("=== HTTP REQUEST ===");
            log.info("URI     : {}", request.getURI());
            log.info("Method  : {}", request.getMethod());
            log.info("Headers : {}", request.getHeaders());
            if (body.length > 0) {
                log.info("Body    : {}", new String(body, StandardCharsets.UTF_8));
            }
            return execution.execute(request, body);
        }));

        log.info("Getting email config from {}, with authToken: {}", uri, authToken);
        ResponseEntity<DestinationServiceResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, entity, DestinationServiceResponse.class);

        return Objects.requireNonNull(response.getBody()).getDestinationConfiguration();
    }

    public String getAuthToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String authUrl = getAuthUrl();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = configureAuthenticationRestTemplate();

        log.info("Get auth token");
        log.info("Url: {}, request:{}", authUrl, request);
        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);
        log.info("Token received: {}", Objects.requireNonNull(response.getBody()).get("access_token").toString());
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
                .additionalInterceptors((request, body, execution) -> {
                    log.info("=== HTTP REQUEST ===");
                    log.info("URI      : {}", request.getURI());
                    log.info("Method   : {}", request.getMethod());
                    log.info("Headers  : {}", request.getHeaders());

                    if (body.length > 0) {
                        log.info("Body     : {}", new String(body, StandardCharsets.UTF_8));
                    }

                    return execution.execute(request, body);
                })
                .build();
    }

}
