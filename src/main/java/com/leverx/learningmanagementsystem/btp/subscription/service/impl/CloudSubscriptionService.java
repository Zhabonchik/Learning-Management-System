package com.leverx.learningmanagementsystem.btp.subscription.service.impl;

import com.leverx.learningmanagementsystem.btp.appinfo.config.ApprouterConfiguration;
import com.leverx.learningmanagementsystem.btp.destinationservice.config.DestinationServiceConfiguration;
import com.leverx.learningmanagementsystem.btp.subscription.model.DependenciesResponseDto;
import com.leverx.learningmanagementsystem.btp.subscription.service.SubscriptionService;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.BindResource;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.Parameters;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaInstanceResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.core.app.config.AppConfiguration;
import com.leverx.learningmanagementsystem.multitenancy.db.service.migrationrunner.DatabaseMigrationRunner;
import com.leverx.learningmanagementsystem.multitenancy.connection.provider.CustomMultiTenantConnectionProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.BINDING;
import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.DB_NAME;
import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.HTTPS_PROTOCOL;
import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.SCHEMA;
import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.SCHEMA_SERVICE_PLAN_ID;
import static com.leverx.learningmanagementsystem.btp.subscription.constants.SubscriptionConstants.TENANT_ID;

@Service
@Profile("cloud")
@Slf4j
@AllArgsConstructor
public class CloudSubscriptionService implements SubscriptionService {

    private final ServiceManager serviceManager;
    private final AppConfiguration appConfiguration;
    private final DatabaseMigrationRunner databaseMigrationRunner;
    private final DestinationServiceConfiguration destinationConfiguration;
    private final ApprouterConfiguration approuterConfiguration;
    private final CustomMultiTenantConnectionProvider connectionProvider;

    @Override
    public String subscribe(String tenantId, String tenantSubDomain) {
        CreateSchemaDto createSchemaDto = configureCreateSchemaDto(tenantId);

        log.info("Assigning schema {} to tenant {}", createSchemaDto.name(), tenantId);
        SchemaInstanceResponse schemaInstance = serviceManager.createServiceInstance(createSchemaDto);

        log.info("Binding schema {} for tenant {}", schemaInstance.id(), tenantId);
        SchemaBindingRequest bindingRequest = configureSchemaBindingRequest(tenantId, schemaInstance.id(), schemaInstance.name());
        serviceManager.bindServiceInstance(bindingRequest);

        databaseMigrationRunner.run(tenantId);

        String appUri = appConfiguration.getUri();
        String appName = appUri.substring(0,appUri.indexOf('.'));

        return "%s://%s-%s".formatted(
                HTTPS_PROTOCOL,
                tenantSubDomain,
                appUri.replace(appName, approuterConfiguration.getName()));
    }

    @Override
    public void unsubscribe(String tenantId) {
        log.info("Getting schema for tenant {} to unsubscribe", tenantId);
        SchemaInstanceResponse schemaInstance = serviceManager.getServiceInstanceByTenantId(tenantId);

        log.info("Getting binding for schema {}", schemaInstance.id());
        SchemaBindingResponse schemaBinding = serviceManager.getServiceBindingByInstanceId(schemaInstance.id());

        log.info("Deleting binding {} for schema {}", schemaBinding.id(), schemaInstance.id());
        serviceManager.unbindServiceInstance(schemaBinding.id());

        log.info("Deleting schema {}", schemaInstance.id());
        serviceManager.deleteServiceInstance(schemaInstance.id());

        connectionProvider.removeDataSource(tenantId);
    }

    @Override
    public List<DependenciesResponseDto> getDependencies() {
        var destinationServiceDependency = DependenciesResponseDto.builder()
                .xsappname(destinationConfiguration.getXsappname())
                .build();

        return List.of(destinationServiceDependency);
    }

    private String configureSchemaName(String tenantId) {
        return SCHEMA.formatted(tenantId);
    }

    private CreateSchemaDto configureCreateSchemaDto(String tenantId) {
        Map<String, List<String>> labels = new HashMap<>();
        labels.put(TENANT_ID, List.of(tenantId));

        Parameters parameters = Parameters.builder()
                .databaseName(DB_NAME)
                .build();

        return CreateSchemaDto.builder()
                .name(configureSchemaName(tenantId))
                .servicePlanId(SCHEMA_SERVICE_PLAN_ID)
                .parameters(parameters)
                .labels(labels)
                .build();
    }

    private SchemaBindingRequest configureSchemaBindingRequest(String tenantId, String schemaInstanceId, String schemaInstanceName) {
        Map<String, List<String>> labels = new HashMap<>();
        labels.put(TENANT_ID, List.of(tenantId));

        BindResource bindResource = BindResource.builder()
                .appGuid(appConfiguration.getApplicationId())
                .spaceGuid(appConfiguration.getSpaceId())
                .build();

        return SchemaBindingRequest.builder()
                .bindingName(BINDING.formatted(schemaInstanceName))
                .serviceInstanceId(schemaInstanceId)
                .bindResource(bindResource)
                .labels(labels)
                .build();
    }
}
