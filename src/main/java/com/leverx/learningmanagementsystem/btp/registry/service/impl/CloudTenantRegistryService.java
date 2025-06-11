package com.leverx.learningmanagementsystem.btp.registry.service.impl;

import com.leverx.learningmanagementsystem.btp.registry.service.TenantRegistryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("cloud")
@Slf4j
public class CloudTenantRegistryService implements TenantRegistryService {
    @Override
    public void subscribeTenant(String tenantId) {
        String schemaName = getSchemaName(tenantId);
        log.info("Assigning schema {} to tenant {}", schemaName, tenantId);
    }

    @Override
    public void unsubscribeTenant(String tenantId) {
        String schemaName = getSchemaName(tenantId);
        log.info("Deleting schema {} to tenant {}", schemaName, tenantId);
    }

    private String getSchemaName(String tenantId) {
        return tenantId;
    }
}
