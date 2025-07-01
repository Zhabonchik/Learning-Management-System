package com.leverx.learningmanagementsystem.multitenancy.subscription.model;

public record SubscriptionRequestDto(
        String subscribedSubdomain,
        String subscribedTenantId
) {
}
