package com.leverx.learningmanagementsystem.btp.registry.model;

import lombok.Builder;

@Builder
public record DependenciesResponseDto (
        String xsappname
) {
}
