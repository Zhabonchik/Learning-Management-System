package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record SchemaInstanceResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("labels") Map<String, List<String>> labels
) {
}
