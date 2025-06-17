package com.leverx.learningmanagementsystem.btp.servicemanager.service.impl;

import com.leverx.learningmanagementsystem.btp.destinationservice.model.DestinationTokenRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.config.ServiceManagerConfiguration;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponseWrapper;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaInstanceResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaInstanceResponseWrapper;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.core.exception.model.BindingException;
import com.leverx.learningmanagementsystem.core.exception.model.SchemaException;
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

import java.util.List;
import java.util.Optional;

import static com.leverx.learningmanagementsystem.btp.servicemanager.constants.ServiceManagerConstants.ASYNC;
import static com.leverx.learningmanagementsystem.btp.servicemanager.constants.ServiceManagerConstants.SERVICE_BINDINGS;
import static com.leverx.learningmanagementsystem.btp.servicemanager.constants.ServiceManagerConstants.SERVICE_INSTANCES;
import static com.leverx.learningmanagementsystem.btp.servicemanager.constants.ServiceManagerConstants.V1;
import static java.util.Objects.nonNull;
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
    public List<SchemaInstanceResponse> getServiceInstances() {
        return tryToGetServiceInstances();
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public SchemaInstanceResponse getServiceInstanceByTenantId(String tenantId) {
        return tryToGetServiceInstanceByTenantId(tenantId);
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public SchemaBindingResponse getServiceBindingByInstanceId(String schemaInstanceId) {
        return tryToGetServiceBindingByInstanceId(schemaInstanceId);
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public SchemaBindingResponse getServiceBindingByTenantId(String tenantId) {
        return tryToGetServiceBindingByTenantId(tenantId);
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public SchemaInstanceResponse createServiceInstance(CreateSchemaDto createSchemaDto) {
        return tryToCreateServiceInstance(createSchemaDto);
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public SchemaBindingResponse bindServiceInstance(SchemaBindingRequest schemaBindingRequest) {
        return tryToBindServiceInstance(schemaBindingRequest);
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public void unbindServiceInstance(String serviceBindingId) {
        tryToUnbindServiceInstance(serviceBindingId);
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public List<SchemaBindingResponse> getServiceBindings() {
        return tryToGetServiceBindings();
    }

    @Override
    @Retryable(
            retryFor = Unauthorized.class,
            maxAttempts = MAX_ATTEMPTS,
            backoff = @Backoff(delay = DELAY)
    )
    public void deleteServiceInstance(String serviceInstanceId) {
        tryToDeleteServiceInstance(serviceInstanceId);
    }

    private List<SchemaInstanceResponse> tryToGetServiceInstances() {
        try {
            String uri = configureServiceInstancesUri();
            var headers = buildHeaders();

            log.info("Trying to get service instances");
            SchemaInstanceResponseWrapper response = restClient.get()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(SchemaInstanceResponseWrapper.class);

            return response.items();
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while getting service instances");
            refreshAuthToken();
            throw ex;
        }
    }

    private SchemaInstanceResponse tryToGetServiceInstanceByTenantId(String tenantId) {
        try {
            List<SchemaInstanceResponse> serviceInstances = tryToGetServiceInstances();
            Optional<SchemaInstanceResponse> response =  serviceInstances.stream()
                    .filter(instance -> {
                        List<String> tenantIds = instance.labels().get("tenantId");
                        return nonNull(tenantIds) && tenantIds.contains(tenantId);
                    })
                    .findFirst();

            if (response.isPresent()) {
                return response.get();
            } else {
                log.info("No schema found for tenant {}", tenantId);
                throw new SchemaException("No schema found for tenant " + tenantId);
            }
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while getting service instance of tenant {}", tenantId);
            refreshAuthToken();
            throw ex;
        }
    }

    private SchemaBindingResponse tryToGetServiceBindingByInstanceId(String schemaInstanceId) {
        try {
            List<SchemaBindingResponse> serviceBindings = tryToGetServiceBindings();
            Optional<SchemaBindingResponse> response =  serviceBindings.stream()
                    .filter(binding -> nonNull(binding.serviceInstanceId()) && binding.serviceInstanceId().equals(schemaInstanceId))
                    .findFirst();

            if (response.isPresent()) {
                return response.get();
            } else {
                log.info("No binding found for schema instance {}", schemaInstanceId);
                throw new SchemaException("No binding found for schema instance" + schemaInstanceId);
            }
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while getting binding for schema instance {}", schemaInstanceId);
            refreshAuthToken();
            throw ex;
        }
    }

    private SchemaBindingResponse tryToGetServiceBindingByTenantId(String tenantId) {
        try {
            List<SchemaBindingResponse> serviceBindings = tryToGetServiceBindings();

            Optional<SchemaBindingResponse> response =  serviceBindings.stream()
                    .filter(binding -> {
                        List<String> tenantIds = binding.labels().get("tenantId");
                        return nonNull(tenantIds) && tenantIds.contains(tenantId);
                    })
                    .findFirst();

            if (response.isPresent()) {
                return response.get();
            } else {
                log.info("No binding found for tenant {}", tenantId);
                throw new BindingException("No binding found for tenant" + tenantId);
            }

        } catch (Unauthorized ex) {
            log.info("Unauthorized access while getting binding for tenant {}", tenantId);
            refreshAuthToken();
            throw ex;
        }
    }

    private SchemaInstanceResponse tryToCreateServiceInstance(CreateSchemaDto createSchemaDto) {
        try {
            String uri = configureServiceInstancesUri();
            var headers = buildHeaders();

            log.info("Trying to create service instances");
            return restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(createSchemaDto)
                    .retrieve()
                    .body(SchemaInstanceResponse.class);
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while creating service instances");
            refreshAuthToken();
            throw ex;
        }
    }

    private SchemaBindingResponse tryToBindServiceInstance(SchemaBindingRequest schemaBindingRequest) {
        try {
            String uri = configureServiceBindingsUri();
            var headers = buildHeaders();

            log.info("Trying to bind service instances");
            log.info("SchemaBindingRequest: {}", schemaBindingRequest);
            return restClient.post()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(schemaBindingRequest)
                    .retrieve()
                    .body(SchemaBindingResponse.class);
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while binding service instances");
            refreshAuthToken();
            throw ex;
        }
    }

    private void tryToUnbindServiceInstance(String serviceBindingId) {
        try {
            String uri = configureServiceUnbindingUri(serviceBindingId);
            var headers = buildHeaders();

            log.info("Trying to unbind service instances");
            restClient.delete()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while unbinding service instances");
            refreshAuthToken();
            throw ex;
        }
    }

    private List<SchemaBindingResponse> tryToGetServiceBindings() {
        try {
            String uri = configureServiceBindingsUri();
            var headers = buildHeaders();

            log.info("Trying to get service bindings");
            SchemaBindingResponseWrapper response = restClient.get()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .body(SchemaBindingResponseWrapper.class);

            return response.items();
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while getting service bindings");
            refreshAuthToken();
            throw ex;
        }
    }

    private void tryToDeleteServiceInstance(String serviceInstanceId) {
        try {
            String uri = configureServiceInstanceUri(serviceInstanceId);
            var headers = buildHeaders();

            log.info("Trying to delete service instance");
            restClient.delete()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Unauthorized ex) {
            log.info("Unauthorized access while deleting service instances");
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
                .queryParam(ASYNC, false)
                .toUriString();
    }

    private String configureServiceInstanceUri(String serviceInstanceId) {
        return UriComponentsBuilder.fromUriString(serviceManagerConfiguration.getSmUrl())
                .pathSegment(V1, SERVICE_INSTANCES, serviceInstanceId)
                .queryParam(ASYNC, false)
                .toUriString();
    }

    private String configureServiceBindingsUri() {
        return UriComponentsBuilder.fromUriString(serviceManagerConfiguration.getSmUrl())
                .pathSegment(V1, SERVICE_BINDINGS)
                .queryParam(ASYNC, false)
                .toUriString();
    }

    private String configureServiceUnbindingUri(String serviceBindingId) {
        return UriComponentsBuilder.fromUriString(serviceManagerConfiguration.getSmUrl())
                .pathSegment(V1, SERVICE_BINDINGS, serviceBindingId)
                .queryParam(ASYNC, false)
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
