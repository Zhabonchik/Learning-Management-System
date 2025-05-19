package com.leverx.learningmanagementsystem.destination.service;

import com.leverx.learningmanagementsystem.config.destination.DestinationServiceConfiguration;
import com.leverx.learningmanagementsystem.destination.model.Destination;
import com.leverx.learningmanagementsystem.token.model.TokenRequest;
import com.leverx.learningmanagementsystem.token.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.DESTINATIONS;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.DESTINATION_CONFIGURATION;
import static com.leverx.learningmanagementsystem.utils.HttpConstantUtils.V1;
import static org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class DestinationService {

    private final long DELAY = 2000;
    private final int MAX_ATTEMPTS = 2;

    private final TokenService tokenService;
    private final RestClient restClient;
    private final DestinationServiceConfiguration destinationServiceConfiguration;

    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public Destination getByName(String name) {
        return tryToGetDestination(name);
    }

    private Destination tryToGetDestination(String name) {
        try {
            String uri = getUri(name);
            var headers = buildHeaders();

            log.info("Getting email config from {}", uri);
            return restClient.get()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(Destination.class);
        } catch (Unauthorized ex) {
            TokenRequest tokenRequest = buildTokenRequest();
            tokenService.refreshAuthToken(tokenRequest);
            throw ex;
        }
    }

    private HttpHeaders buildHeaders() {
        TokenRequest tokenRequest = buildTokenRequest();
        String authToken = tokenService.getAuthToken(tokenRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }

    private String getUri(String destinationName) {
        log.info("Configure uri");
        return UriComponentsBuilder.fromUriString(destinationServiceConfiguration.getUri())
                .pathSegment(DESTINATION_CONFIGURATION, V1, DESTINATIONS, destinationName)
                .toUriString();
    }

    private TokenRequest buildTokenRequest() {
        return new TokenRequest(
                destinationServiceConfiguration.getUrl(),
                destinationServiceConfiguration.getClientId(),
                destinationServiceConfiguration.getClientSecret()
        );
    }
}
