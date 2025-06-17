package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.destinationservice.config.DestinationServiceConfiguration;
import com.leverx.learningmanagementsystem.btp.registry.model.DependenciesResponseDto;
import com.leverx.learningmanagementsystem.btp.registry.service.SubscriptionService;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.BindResource;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.Parameters;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaInstanceResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.core.app.config.AppConfiguration;
import com.leverx.learningmanagementsystem.db.service.dbmigrator.DataBaseMigrator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.BINDING;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.DB_NAME;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.INSTANCE_CREATION_TIMEOUT_SEC;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.INSTANCE_DELETION_TIMEOUT_SEC;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.ROUTER_URL;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.SCHEMA;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.SERVICE_BINDING_TIMEOUT_SEC;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.SERVICE_PLAN_ID;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.SERVICE_UNBINDING_TIMEOUT_SEC;
import static com.leverx.learningmanagementsystem.btp.registry.constants.RegistryConstants.TENANT_ID;

@Service
@Profile("cloud")
@Slf4j
@AllArgsConstructor
public class CloudSubscriptionService implements SubscriptionService {

    private final ServiceManager serviceManager;
    private final AppConfiguration appConfiguration;
    private final DataBaseMigrator dataBaseMigrator;
    private final DestinationServiceConfiguration destinationConfiguration;

    @Override
    public String subscribe(String tenantId, String tenantSubDomain) {
        CreateSchemaDto createSchemaDto = configureCreateSchemaDto(tenantId);

        log.info("Assigning schema {} to tenant {}", createSchemaDto.name(), tenantId);
        SchemaInstanceResponse schemaInstance = serviceManager.createServiceInstance(createSchemaDto);

        log.info("Binding schema {} for tenant {}", schemaInstance.id(), tenantId);
        SchemaBindingRequest bindingRequest = configureSchemaBindingRequest(tenantId, schemaInstance.id(), schemaInstance.name());
        serviceManager.bindServiceInstance(bindingRequest);

        dataBaseMigrator.migrateSchemaOnStartUp(tenantId);

        return ROUTER_URL.formatted(tenantSubDomain);
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
                .servicePlanId(SERVICE_PLAN_ID)
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
