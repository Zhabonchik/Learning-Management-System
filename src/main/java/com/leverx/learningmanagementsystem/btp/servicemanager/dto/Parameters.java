package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record Parameters(
        @JsonProperty("databaseName") String databaseName
) {
}
