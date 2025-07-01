package com.leverx.learningmanagementsystem.multitenancy.subscription.service;

import com.leverx.learningmanagementsystem.multitenancy.subscription.model.DependenciesResponseDto;

import java.util.List;

public interface SubscriptionService {

    String subscribe(String tenantId, String tenantSubDomain);

    void unsubscribe(String tenantId);

    List<DependenciesResponseDto> getDependencies();
}
