package com.leverx.learningmanagementsystem.btp.servicemanager.service;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingRequest;
import com.leverx.learningmanagementsystem.btp.servicemanager.dto.SchemaBindingResponse;
import com.leverx.learningmanagementsystem.btp.servicemanager.model.SchemaInstance;

import java.util.List;

public interface ServiceManager {

    List<SchemaInstance> getServiceInstances();

    void createServiceInstance(CreateSchemaDto createSchemaDto);

    void bindService(SchemaBindingRequest schemaBindingRequest);

    void unbindService(String serviceBindingId);

    List<SchemaBindingResponse> getServiceBindings();

    void deleteServiceInstance(String serviceInstanceId);
}
