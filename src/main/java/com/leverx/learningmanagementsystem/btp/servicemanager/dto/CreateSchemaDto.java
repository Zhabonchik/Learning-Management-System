package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

public record CreateSchemaDto (
        String instanceName,
        String servicePlanId,
        String databaseName,
        String tenantId
) {
}
