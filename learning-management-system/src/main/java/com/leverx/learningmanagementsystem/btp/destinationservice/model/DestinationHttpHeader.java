package com.leverx.learningmanagementsystem.btp.destinationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DestinationHttpHeader(
        @JsonProperty("key") String key,
        @JsonProperty("value") String value) {
}