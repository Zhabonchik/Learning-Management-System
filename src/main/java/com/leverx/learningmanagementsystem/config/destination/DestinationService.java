package com.leverx.learningmanagementsystem.config.destination;

import com.leverx.learningmanagementsystem.email.DestinationServiceMailConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    public DestinationServiceMailConfig getEmailConfig(String destinationName) {
        String url = getUrl(destinationName);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getAuthToken());

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        log.info("Getting email config from {}", url);
        return restTemplate.getForObject(url, DestinationServiceMailConfig.class, entity);
    }

    private String getUrl(String destinationName) {
        return UriComponentsBuilder.fromUriString(destinationConfig.getUri())
                .pathSegment("destination-configuration", "v1", "destinations", destinationName)
                .toUriString();
    }

    private String getAuthToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = configureAuthenticationRestTemplate();

        log.info("Get auth token");
        ResponseEntity<Map> response = restTemplate.postForEntity(destinationConfig.getUrl(), request, Map.class);
        log.info("Token received: {}", Objects.requireNonNull(response.getBody()).get("access_token").toString());
        return Objects.requireNonNull(response.getBody()).get("access_token").toString();
    }

    private RestTemplate configureAuthenticationRestTemplate() {
        return restTemplateBuilder
                .basicAuthentication(destinationConfig.getClientId(), destinationConfig.getClientSecret())
                .build();
    }

}
