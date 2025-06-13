package com.leverx.learningmanagementsystem.btp.registry.service;

public interface SubscriptionService {

    String subscribe(String tenantId, String tenantSubDomain);

    void unsubscribe(String tenantId);
}
