package com.leverx.learningmanagementsystem.btp.registry.service;

public interface TenantRegistryService {

    void subscribeTenant(String tenantSubdomain);

    void unsubscribeTenant(String tenantSubdomain);
}
