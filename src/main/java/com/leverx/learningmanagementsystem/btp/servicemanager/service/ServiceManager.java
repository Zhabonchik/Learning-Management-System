package com.leverx.learningmanagementsystem.btp.servicemanager.service;

import com.leverx.learningmanagementsystem.btp.servicemanager.dto.CreateSchemaDto;
import com.leverx.learningmanagementsystem.btp.servicemanager.model.SchemaInstance;

import java.util.List;

public interface ServiceManager {

    void createServiceInstance(CreateSchemaDto createSchemaDto);

    List<SchemaInstance> getServiceInstances();
}
