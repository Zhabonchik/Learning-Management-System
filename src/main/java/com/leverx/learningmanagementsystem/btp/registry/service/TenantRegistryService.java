package com.leverx.learningmanagementsystem.btp.registry.service;

public interface TenantRegistryService {

    String subscribeTenant(String tenantId, String tenantSubDomain);

    void unsubscribeTenant(String tenantId);
}
