package com.leverx.learningmanagementsystem.token.service;

import com.leverx.learningmanagementsystem.token.model.TokenRequest;
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
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
import java.util.Map;

import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.ACCESS_TOKEN;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.AUTHORIZATION;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.BASIC;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.CLIENT_CREDENTIALS;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.GRANT_TYPE;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.OAUTH;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.TOKEN;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

@Service
@Slf4j
@AllArgsConstructor
@Profile("cloud")
public class TokenService {

    private final RestClient restClient;

    @Cacheable(value = "authTokens", key = "#tokenRequest.clientId()")
    public String getAuthToken(TokenRequest tokenRequest) {
        log.info("Get auth token");
        return requestAuthToken(tokenRequest);
    }

    @CachePut(value = "authTokens", key = "#tokenRequest.clientId()")
    public String refreshAuthToken(TokenRequest tokenRequest) {
        log.info("Refresh auth token");
        return requestAuthToken(tokenRequest);
    }

    private String requestAuthToken(TokenRequest tokenRequest) {
        String authUrl = getAuthUrl(tokenRequest.tokenUrl());
        HttpEntity<MultiValueMap<String, String>> request = configureHttpEntity(tokenRequest);

        log.info("Request auth token");
        ResponseEntity<Map> response = restClient.post()
                .uri(authUrl)
                .body(request)
                .retrieve()
                .toEntity(Map.class);

        return response.getBody()
                .get(ACCESS_TOKEN)
                .toString()
                .trim()
                .replaceAll("[\\n\\r]", "");
    }

    private String getAuthUrl(String tokenUrl) {
        log.info("Get auth url");
        return UriComponentsBuilder.fromUriString(tokenUrl)
                .pathSegment(OAUTH, TOKEN)
                .toUriString();
    }

    private HttpEntity<MultiValueMap<String, String>> configureHttpEntity(TokenRequest tokenRequest) {
        log.info("Configure http entity");
        HttpHeaders headers = configureHeaders(tokenRequest);
        MultiValueMap<String, String> body = configureBody();
        return new HttpEntity<>(body, headers);
    }

    private HttpHeaders configureHeaders(TokenRequest tokenRequest) {
        log.info("Configure headers");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_FORM_URLENCODED);

        String auth = "%s:%s".formatted(tokenRequest.clientId(), tokenRequest.clientSecret());
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
