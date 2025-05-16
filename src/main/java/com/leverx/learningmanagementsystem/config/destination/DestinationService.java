package com.leverx.learningmanagementsystem.config.destination;

import com.leverx.learningmanagementsystem.config.token.TokenService;
import com.leverx.learningmanagementsystem.email.mailconfig.Destination;
import com.leverx.learningmanagementsystem.email.DestinationServiceResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.leverx.learningmanagementsystem.utils.RetryUtils.MAX_ATTEMPTS;
import static com.leverx.learningmanagementsystem.utils.RetryUtils.DELAY;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class DestinationService {

    private final TokenService tokenService;
    private final RestTemplate restTemplate;

    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public Destination getByName(String destinationName) {
        String uri = tokenService.getUri(destinationName);
        String authToken = tokenService.getAuthToken();

        return sendRequest(uri, authToken);
    }

    @Recover
    public Destination recover(Unauthorized ex, String destinationName) {
        log.info("All retries failed with 401. Refreshing token from recover...");

        String uri = tokenService.getUri(destinationName);
        String authToken = tokenService.refreshAuthToken();

        try {
            return sendRequest(uri, authToken);
        } catch (Exception retryException) {
            log.info("Retry after token refresh failed", retryException);
            throw retryException;
        }
    }

    private Destination sendRequest(String uri, String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        log.info("Getting email config from {}", uri);
        ResponseEntity<DestinationServiceResponse> response = restTemplate
                .exchange(uri, GET, entity, DestinationServiceResponse.class);

        return response.getBody().getDestinationConfiguration();
    }

}
