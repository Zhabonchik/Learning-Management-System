package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.registry.service.TenantRegistryService;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.BindResource;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.Parameters;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaInstanceResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.service.ServiceManager;
import com.leverx.learningmanagementsystem.core.app.config.AppConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.BINDING;
import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.DB_NAME;
import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.ROUTER_URL;
import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.SCHEMA;
import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.SERVICE_PLAN_ID;
import static com.leverx.learningmanagementsystem.btp.registry.utils.RegistryUtils.TENANT_ID;

@Service
@Profile("cloud")
@Slf4j
@AllArgsConstructor
public class CloudTenantRegistryService implements TenantRegistryService {

    private final ServiceManager serviceManager;
    private final AppConfiguration appConfiguration;

    @Override
    public String subscribeTenant(String tenantId, String tenantSubDomain) {
        CreateSchemaDto createSchemaDto = configureCreateSchemaDto(tenantId);

        log.info("Assigning schema {} to tenant {}", createSchemaDto.name(), tenantId);
        serviceManager.createServiceInstance(createSchemaDto);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.info("Interrupted while waiting for service instance creation");
        }

        log.info("Getting schema for tenant {}", tenantId);
        SchemaInstanceResponse schemaInstance = serviceManager.getServiceInstanceByTenantId(tenantId);

        log.info("Binding schema {} for tenant {}", schemaInstance.id(), tenantId);
        SchemaBindingRequest bindingRequest = configureSchemaBindingRequest(tenantId, schemaInstance.id(), schemaInstance.name());
        serviceManager.bindServiceInstance(bindingRequest);
        return ROUTER_URL.formatted(tenantSubDomain);
    }

    @Override
    public void unsubscribeTenant(String tenantId) {
        log.info("Getting schema for tenant {} to unsubscribe", tenantId);
        SchemaInstanceResponse schemaInstance = serviceManager.getServiceInstanceByTenantId(tenantId);

        log.info("Getting binding for schema {}", schemaInstance.id());
        SchemaBindingResponse schemaBinding = serviceManager.getServiceBindingByInstanceId(schemaInstance.id());

        log.info("Deleting binding {} for schema {}", schemaBinding.id(), schemaInstance.id());
        serviceManager.unbindServiceInstance(schemaBinding.id());
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.info("Interrupted while waiting for service unbinding");
        }

        log.info("Deleting schema {}", schemaInstance.id());
        serviceManager.deleteServiceInstance(schemaInstance.id());
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
