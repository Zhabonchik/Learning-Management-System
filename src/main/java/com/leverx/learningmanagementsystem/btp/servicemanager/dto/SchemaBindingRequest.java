package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record SchemaBindingRequest(
        @JsonProperty("name") String bindingName,
        @JsonProperty("service_instance_id") String serviceInstanceId,
        @JsonProperty("bind_resource") BindResource bindResource,
        @JsonProperty("labels") Map<String, List<String>> labels
        ) {
}
