package com.leverx.learningmanagementsystem.btp.servicemanager.service;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaInstanceResponse;

import java.util.List;

public interface ServiceManager {

    List<SchemaInstanceResponse> getServiceInstances();

    List<SchemaBindingResponse> getServiceBindings();

    SchemaInstanceResponse getServiceInstanceByTenantId(String tenantId);

    SchemaBindingResponse getServiceBindingByInstanceId(String schemaInstanceId);

    SchemaBindingResponse getServiceBindingByTenantId(String tenantId);

    SchemaInstanceResponse createServiceInstance(CreateSchemaDto createSchemaDto);

    SchemaBindingResponse bindServiceInstance(SchemaBindingRequest schemaBindingRequest);

    void unbindServiceInstance(String serviceBindingId);

    void deleteServiceInstance(String serviceInstanceId);
}
