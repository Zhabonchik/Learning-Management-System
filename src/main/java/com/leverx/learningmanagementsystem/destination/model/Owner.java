package com.leverx.learningmanagementsystem.destination.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Owner(
        @JsonProperty("SubAccountId") String subAccountId,
        @JsonProperty("InstanceId") String instanceId) {
}
