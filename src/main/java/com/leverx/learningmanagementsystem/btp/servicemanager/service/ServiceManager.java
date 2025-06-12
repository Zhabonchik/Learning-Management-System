package com.leverx.learningmanagementsystem.btp.servicemanager.service;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaInstanceResponse;

import java.util.List;

public interface ServiceManager {

    List<SchemaInstanceResponse> getServiceInstances();

    SchemaInstanceResponse getServiceInstanceByTenantId(String tenantId);

    SchemaBindingResponse getServiceBindingByInstanceId(String schemaInstanceId);

    void createServiceInstance(CreateSchemaDto createSchemaDto);

    void bindServiceInstance(SchemaBindingRequest schemaBindingRequest);

    void unbindServiceInstance(String serviceBindingId);

    List<SchemaBindingResponse> getServiceBindings();

    void deleteServiceInstance(String serviceInstanceId);
}
