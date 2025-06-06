package com.leverx.learningmanagementsystem.btp.registry.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegistryResponseDto(
        @JsonProperty("app-url") String url,
        String infoEndpoint
) {
}
