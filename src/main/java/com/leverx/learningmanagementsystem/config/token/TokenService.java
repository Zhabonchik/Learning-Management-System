package com.leverx.learningmanagementsystem.config.token;

import com.leverx.learningmanagementsystem.config.destination.DestinationConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.Map;

import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.ACCESS_TOKEN;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.AUTHORIZATION;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.BASIC;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.CLIENT_CREDENTIALS;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.DESTINATIONS;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.DESTINATION_CONFIGURATION;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.GRANT_TYPE;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.OAUTH;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.TOKEN;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.V1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
@Slf4j
@AllArgsConstructor
@Profile("cloud")
public class TokenService {

    private final DestinationConfiguration destinationConfiguration;
    private final RestTemplate restTemplate;

    @Cacheable("authTokens")
    public String getAuthToken() {
        log.info("Get auth token");
        return requestAuthToken();
    }

    @CachePut("authTokens")
    public String refreshAuthToken() {
        log.info("Refresh auth token");
        return requestAuthToken();
    }

    public String getUri(String destinationName) {
        log.info("Configure uri");
        return UriComponentsBuilder.fromUriString(destinationConfiguration.getUri())
                .pathSegment(DESTINATION_CONFIGURATION, V1, DESTINATIONS, destinationName)
                .toUriString();
    }

    private String requestAuthToken() {
        String authUrl = getAuthUrl();
        HttpEntity<MultiValueMap<String, String>> request = configureHttpEntity();

        log.info("Request auth token");
        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);

        return response.getBody()
                .get(ACCESS_TOKEN)
                .toString()
                .trim()
                .replaceAll("[\\n\\r]", "");
    }

    private String getAuthUrl() {
        log.info("Get auth url");
        return UriComponentsBuilder.fromUriString(destinationConfiguration.getUrl())
                .pathSegment(OAUTH, TOKEN)
                .toUriString();
    }

    private HttpEntity<MultiValueMap<String, String>> configureHttpEntity() {
        log.info("Configure http entity");
        HttpHeaders headers = configureHeaders();
        MultiValueMap<String, String> body = configureBody();
        return new HttpEntity<>(body, headers);
    }

    private HttpHeaders configureHeaders() {
        log.info("Configure headers");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String auth = "%s:%s".formatted(destinationConfiguration.getClientId(), destinationConfiguration.getClientSecret());
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(UTF_8));

        headers.set(AUTHORIZATION, BASIC + " " + encodedAuth);
        return headers;
    }

    private MultiValueMap<String, String> configureBody() {
        log.info("Configure body");
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add(GRANT_TYPE, CLIENT_CREDENTIALS);
        return body;
    }
}
