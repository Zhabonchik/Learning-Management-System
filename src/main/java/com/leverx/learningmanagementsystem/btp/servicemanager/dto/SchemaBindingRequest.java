package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record SchemaBindingRequest(
        @JsonProperty("name") String bindingName,
        @JsonProperty("service_instance_id") String serviceInstanceId,
        @JsonProperty("bind_resource") BindResource bindResource,
        @JsonProperty("labels") Map<String, List<String>> labels
        ) {
}
