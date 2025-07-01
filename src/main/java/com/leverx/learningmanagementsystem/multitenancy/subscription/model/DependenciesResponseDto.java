package com.leverx.learningmanagementsystem.multitenancy.subscription.model;

import lombok.Builder;

@Builder
public record DependenciesResponseDto (
        String xsappname
) {
}
