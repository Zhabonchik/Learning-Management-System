package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record CreateSchemaDto (
        @JsonProperty("name") String name,
        @JsonProperty("service_plan_id") String servicePlanId,
        @JsonProperty("parameters") Parameters parameters,
        @JsonProperty("labels") Map<String, List<String>> labels
) {
}
