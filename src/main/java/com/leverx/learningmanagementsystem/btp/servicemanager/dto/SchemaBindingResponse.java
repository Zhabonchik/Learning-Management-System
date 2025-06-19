package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record SchemaBindingResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("service_instance_id") String serviceInstanceId,
        @JsonProperty("bind_resource") BindResource bindResource,
        @JsonProperty("labels") Map<String, List<String>> labels,
        @JsonProperty("credentials") Map<String, String> credentials
) {
}
