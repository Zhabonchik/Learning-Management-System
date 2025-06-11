package com.leverx.learningmanagementsystem.btp.servicemanager.service.impl;

import com.leverx.learningmanagementsystem.btp.destinationservice.model.DestinationTokenRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.config.ServiceManagerConfiguration;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.model.SchemaInstance;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.web.oauth.token.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.leverx.learningmanagementsystem.btp.servicemanager.utils.ServiceManagerUtils.SERVICE_BINDINGS;
import static com.leverx.learningmanagementsystem.btp.servicemanager.utils.ServiceManagerUtils.SERVICE_INSTANCES;
import static com.leverx.learningmanagementsystem.btp.servicemanager.utils.ServiceManagerUtils.V1;
import static org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Service
@AllArgsConstructor
@Slf4j
@Profile("cloud")
public class ServiceManagerImpl implements ServiceManager {

    public static final long DELAY = 2000;
    public static final int MAX_ATTEMPTS = 2;

    private final RestClient restClient;
    private final ServiceManagerConfiguration serviceManagerConfiguration;
    private final TokenService tokenService;

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public List<SchemaInstance> getServiceInstances() {
        return tryToGetServiceInstances();
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public void createServiceInstance(CreateSchemaDto createSchemaDto) {
        tryToCreateServiceInstance(createSchemaDto);
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public void bindService(SchemaBindingRequest schemaBindingRequest) {
        tryToBindServiceInstance(schemaBindingRequest);
    }

    @Override
    public void unbindService(String serviceBindingId) {

    }

    @Override
    public List<SchemaBindingResponse> getServiceBindings() {
        return List.of();
    }

    @Override
    public void deleteServiceInstance(String serviceInstanceId) {

    }

    private List<SchemaInstance> tryToGetServiceInstances() {
        try {
            String uri = configureServiceInstancesUri();
            var headers = buildHeaders();

            log.info("Trying to get service instances");
            return restClient.get()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<SchemaInstance>>() {});
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while getting service instances");
            refreshAuthToken();
            throw ex;
        }
    }

    private void tryToCreateServiceInstance(CreateSchemaDto createSchemaDto) {
        try {
            String uri = configureServiceInstancesUri();
            var headers = buildHeaders();

            log.info("Trying to create service instances");
            restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(createSchemaDto);
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while creating service instances");
            refreshAuthToken();
            throw ex;
        }
    }

    private void tryToBindServiceInstance(SchemaBindingRequest schemaBindingRequest) {
        try {
            String uri = configureServiceBindingsUri();
            var headers = buildHeaders();

            log.info("Trying to bind service instances");
            restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(schemaBindingRequest);
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while binding service instances");
            refreshAuthToken();
            throw ex;
        }
    }

    private HttpHeaders buildHeaders() {
        var tokenRequest = configureTokenRequest();
        String authToken = tokenService.getAuthToken(tokenRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return headers;
    }

    private String configureServiceInstancesUri() {
        return UriComponentsBuilder.fromUriString(serviceManagerConfiguration.getSmUrl())
                .pathSegment(V1, SERVICE_INSTANCES)
                .toUriString();
    }

    private String configureServiceBindingsUri() {
        return UriComponentsBuilder.fromUriString(serviceManagerConfiguration.getSmUrl())
                .pathSegment(V1, SERVICE_BINDINGS)
                .toUriString();
    }

    private DestinationTokenRequest configureTokenRequest() {
        return new DestinationTokenRequest(
                serviceManagerConfiguration.getUrl(),
                serviceManagerConfiguration.getClientId(),
                serviceManagerConfiguration.getClientSecret()
        );
    }

    private void refreshAuthToken() {
        var tokenRequest = configureTokenRequest();
        tokenService.refreshAuthToken(tokenRequest);
    }
}
