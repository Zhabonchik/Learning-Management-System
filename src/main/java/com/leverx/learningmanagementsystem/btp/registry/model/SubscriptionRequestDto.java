package com.leverx.learningmanagementsystem.btp.registry.model;

public record SubscriptionRequestDto(
        String subscribedSubdomain,
        String subscribedTenantId
) {
}
