package com.leverx.learningmanagementsystem.btp.subscription.service;

import com.leverx.learningmanagementsystem.btp.subscription.model.DependenciesResponseDto;

import java.util.List;

public interface SubscriptionService {

    String subscribe(String tenantId, String tenantSubDomain);

    void unsubscribe(String tenantId);
}
