package com.leverx.learningmanagementsystem.btp.destinationservice.service;

import com.leverx.learningmanagementsystem.btp.destinationservice.config.DestinationServiceConfiguration;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.Destination;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.DestinationTokenRequest;
import com.leverx.learningmanagementsystem.web.oauth.token.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import static com.leverx.learningmanagementsystem.btp.destinationservice.utils.DestinationServiceUtils.DESTINATIONS;
import static com.leverx.learningmanagementsystem.btp.destinationservice.utils.DestinationServiceUtils.DESTINATION_CONFIGURATION;
import static com.leverx.learningmanagementsystem.btp.destinationservice.utils.DestinationServiceUtils.V1;
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
            DestinationTokenRequest destinationTokenRequest = buildTokenRequest();
            tokenService.refreshAuthToken(destinationTokenRequest);
            throw ex;
        }
    }

    private HttpHeaders buildHeaders() {
        DestinationTokenRequest destinationTokenRequest = buildTokenRequest();
        String authToken = tokenService.getAuthToken(destinationTokenRequest);
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

    private DestinationTokenRequest buildTokenRequest() {
        return new DestinationTokenRequest(
                destinationServiceConfiguration.getUrl(),
                destinationServiceConfiguration.getClientId(),
                destinationServiceConfiguration.getClientSecret()
        );
    }
}
