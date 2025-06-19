package com.leverx.learningmanagementsystem.btp.subscription.model;

public record SubscriptionRequestDto(
        String subscribedSubdomain,
        String subscribedTenantId
) {
}
