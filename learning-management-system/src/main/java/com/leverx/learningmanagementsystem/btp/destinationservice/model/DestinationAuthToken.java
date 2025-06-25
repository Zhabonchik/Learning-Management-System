package com.leverx.learningmanagementsystem.btp.destinationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DestinationAuthToken(
        @JsonProperty("type") String type,
        @JsonProperty("value") String value,
        @JsonProperty("http_header") DestinationHttpHeader httpHeader) {
}
