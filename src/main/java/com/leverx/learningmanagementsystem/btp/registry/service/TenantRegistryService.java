package com.leverx.learningmanagementsystem.btp.registry.service;

public interface TenantRegistryService {

    void subscribeTenant(String tenantId);

    void unsubscribeTenant(String tenantId);
}
