package com.leverx.learningmanagementsystem.btp.destinationservice.service;

import com.leverx.learningmanagementsystem.btp.destinationservice.config.DestinationServiceConfiguration;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.Destination;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.DestinationConfiguration;
import com.leverx.learningmanagementsystem.web.oauth.token.model.TokenRequest;
import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import com.leverx.learningmanagementsystem.web.oauth.token.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import static com.leverx.learningmanagementsystem.btp.destinationservice.constants.DestinationServiceConstants.DESTINATION_CONFIGURATION;
import static com.leverx.learningmanagementsystem.btp.destinationservice.constants.DestinationServiceConstants.INSTANCE_DESTINATIONS;
import static com.leverx.learningmanagementsystem.btp.destinationservice.constants.DestinationServiceConstants.SUBACCOUNT_DESTINATIONS;
import static com.leverx.learningmanagementsystem.btp.destinationservice.constants.DestinationServiceConstants.V1;
import static java.util.Objects.nonNull;
import static org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class DestinationService {

    public static final long DELAY = 2000;
    public static final int MAX_ATTEMPTS = 3;

    private final TokenService tokenService;
    private final RestClient restClient;
    private final DestinationServiceConfiguration destinationServiceConfiguration;

    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public DestinationConfiguration getByName(String name) {
        String subdomain = RequestContext.getTenantSubdomain();

        if (nonNull(subdomain)) {
            DestinationConfiguration destination = tryToGetSubaccountDestination(name, subdomain);

            if (nonNull(destination)) {
                return destination;
            }
        }

        log.info("Retrieving destination by provider account");
        return tryToGetDestination(name, INSTANCE_DESTINATIONS, destinationServiceConfiguration.getUrl());
    }

    private DestinationConfiguration tryToGetSubaccountDestination(String name, String subdomain) {
        try {
            log.info("Retrieving destination by subAccount {}", subdomain);
            String tokenUrl = replaceProviderWithSubscriberUrl(destinationServiceConfiguration.getUrl());

            return tryToGetDestination(name, SUBACCOUNT_DESTINATIONS, tokenUrl);
        } catch (HttpClientErrorException.NotFound ex) {
            log.info("Destination {} not found in subAccount {}", name, subdomain);
            return null;
        }
    }

    private DestinationConfiguration tryToGetDestination(String name, String destinationsType, String tokenUrl) {
        try {
            log.info("Trying to get destination {}", name);
            String uri = getUri(name, destinationsType);
            log.info("URI: {}", uri);
            var headers = buildHeaders(tokenUrl);

            log.info("Getting email config from {}", uri);
            return restClient.get()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(DestinationConfiguration.class);
        } catch (Unauthorized ex) {
            TokenRequest tokenRequest = buildTokenRequest(tokenUrl);
            tokenService.refreshAuthToken(tokenRequest);
            throw ex;
        }
    }

    private HttpHeaders buildHeaders(String tokenUrl) {
        TokenRequest tokenRequest = buildTokenRequest(tokenUrl);
        String authToken = tokenService.getAuthToken(tokenRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }

    private String getUri(String destinationName, String destinationsType) {
        log.info("Configure uri");
        return UriComponentsBuilder.fromUriString(destinationServiceConfiguration.getUri())
                .pathSegment(DESTINATION_CONFIGURATION, V1, destinationsType, destinationName)
                .toUriString();
    }

    private TokenRequest buildTokenRequest(String url) {
        log.info("Building token request with url {}", url);
        return new TokenRequest(
                url,
                destinationServiceConfiguration.getClientId(),
                destinationServiceConfiguration.getClientSecret()
        );
    }

    private String replaceProviderWithSubscriberUrl(String url) {
        String subdomain = RequestContext.getTenantSubdomain();

        return url.replaceFirst("https://[^.]+", "https://" + subdomain);
    }
}
