package com.leverx.learningmanagementsystem.btp.destinationservice.service;

import com.leverx.learningmanagementsystem.btp.destinationservice.model.DestinationServiceProperties;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.Destination;
import com.leverx.learningmanagementsystem.btp.destinationservice.model.DestinationRequest;
import com.leverx.learningmanagementsystem.core.exception.model.LearningManagementException;
import com.leverx.learningmanagementsystem.core.security.context.RequestContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.function.Supplier;

import static com.leverx.learningmanagementsystem.btp.destinationservice.constants.DestinationServiceConstants.DESTINATIONS;
import static com.leverx.learningmanagementsystem.btp.destinationservice.constants.DestinationServiceConstants.DESTINATION_CONFIGURATION;
import static com.leverx.learningmanagementsystem.btp.destinationservice.constants.DestinationServiceConstants.V1;
import static org.springframework.web.client.HttpClientErrorException.NotFound;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class DestinationService {

    private final DestinationServiceClient destinationServiceClient;
    private final DestinationServiceProperties destinationServiceProperties;

    public Destination getByName(String name) {
        return findDestinationOnSubscriberLevel(name)
                .or(() -> findDestinationOnProviderLevel(name))
                .orElseThrow(() -> new LearningManagementException("Destination is not found [name = %s]".formatted(name)));
    }

    private Optional<Destination> findDestinationOnSubscriberLevel(String name) {
        log.info("Finding destination on subscriber level [name = {}]", name);
        Supplier<DestinationRequest> tokenUrlSupplier = this::buildClientCredentialsForSubscriber;
        return tryToGetDestination(name, tokenUrlSupplier);
    }

    private Optional<Destination> findDestinationOnProviderLevel(String name) {
        log.info("Finding destination on provider level [name = {}]", name);
        Supplier<DestinationRequest> tokenUrlSupplier = this::buildClientCredentialsForProvider;
        return tryToGetDestination(name, tokenUrlSupplier);
    }

    private DestinationRequest buildClientCredentialsForSubscriber() {
        String uri = buildUri(destinationServiceProperties.getUri());
        String tokenUrl = replaceProviderWithSubscriberUrl(destinationServiceProperties.getTokenUrl());

        return new DestinationRequest(
                uri,
                tokenUrl,
                destinationServiceProperties.getClientId(),
                destinationServiceProperties.getClientSecret());
    }

    private DestinationRequest buildClientCredentialsForProvider() {
        String uri = buildUri(destinationServiceProperties.getUri());

        return new DestinationRequest(
                uri,
                destinationServiceProperties.getTokenUrl(),
                destinationServiceProperties.getClientId(),
                destinationServiceProperties.getClientSecret());
    }

    private Optional<Destination> tryToGetDestination(String name, Supplier<DestinationRequest> clientCredentialsSupplier) {
        try {
            log.info("Trying to get destination [name = {}]", name);
            DestinationRequest clientCredentials = clientCredentialsSupplier.get();

            return Optional.ofNullable(destinationServiceClient.get(name, clientCredentials));
        } catch (NotFound ex) {
            return Optional.empty();
        }
    }

    private String replaceProviderWithSubscriberUrl(String url) {
        String subdomain = RequestContext.getTenantSubdomain();
        return url.replaceFirst("https://[^.]+", "https://" + subdomain);
    }

    private String buildUri(String destinationName) {
        return UriComponentsBuilder.fromUriString(destinationServiceProperties.getUri())
                .pathSegment(DESTINATION_CONFIGURATION, V1, DESTINATIONS, destinationName)
                .toUriString();
    }
}
