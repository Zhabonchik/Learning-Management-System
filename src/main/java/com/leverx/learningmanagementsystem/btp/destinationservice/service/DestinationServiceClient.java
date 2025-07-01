package com.leverx.learningmanagementsystem.btp.destinationservice.service;

import com.leverx.learningmanagementsystem.btp.destinationservice.model.Destination;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.DestinationRequest;
import com.leverx.learningmanagementsystem.web.oauth.token.model.TokenRequest;
import com.leverx.learningmanagementsystem.web.oauth.token.service.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.RestClient;

@Service
@AllArgsConstructor
@Profile("cloud")
public class DestinationServiceClient {

    public static final long DELAY = 2000;
    public static final int MAX_ATTEMPTS = 3;

    private final RestClient restClient;
    private final TokenService tokenService;

    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public Destination get(String name, DestinationRequest destinationRequest) {
        try {
            String uri = destinationRequest.getUri();
            String token = getToken(destinationRequest);

            return restClient.get()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
                    .retrieve()
                    .body(Destination.class);
        } catch (Unauthorized ex) {
            refreshToken(destinationRequest);
            throw ex;
        }
    }

    private TokenRequest buildTokenRequest(DestinationRequest destinationRequest) {
        return new TokenRequest(
                destinationRequest.getTokenUrl(),
                destinationRequest.getClientId(),
                destinationRequest.getClientSecret()
        );
    }

    private String getToken(DestinationRequest destinationRequest) {
        TokenRequest tokenRequest = buildTokenRequest(destinationRequest);

        return tokenService.getAuthToken(tokenRequest);
    }

    private void refreshToken(DestinationRequest destinationRequest) {
        TokenRequest tokenRequest = buildTokenRequest(destinationRequest);
        tokenService.refreshAuthToken(tokenRequest);
    }
}
