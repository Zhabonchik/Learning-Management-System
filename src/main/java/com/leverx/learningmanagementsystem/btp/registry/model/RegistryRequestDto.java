package com.leverx.learningmanagementsystem.btp.registry.model;

public record RegistryRequestDto(
        String subscribedSubdomain,
        String subscribedTenantId
) {
}
