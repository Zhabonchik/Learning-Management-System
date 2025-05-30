package com.leverx.learningmanagementsystem.cloud.destinationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Owner(
        @JsonProperty("SubAccountId") String subAccountId,
        @JsonProperty("InstanceId") String instanceId) {
}
