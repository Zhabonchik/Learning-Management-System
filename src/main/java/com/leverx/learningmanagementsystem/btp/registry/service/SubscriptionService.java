package com.leverx.learningmanagementsystem.btp.registry.service;

import com.leverx.learningmanagementsystem.btp.registry.model.DependenciesResponseDto;

import java.util.List;

public interface SubscriptionService {

    String subscribe(String tenantId, String tenantSubDomain);

    void unsubscribe(String tenantId);

    List<DependenciesResponseDto> getDependencies();
}
