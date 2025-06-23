package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SchemaBindingResponseWrapper(
        @JsonProperty("num_items") String numItems,
        @JsonProperty("items") List<SchemaBindingResponse> items
) {
}
