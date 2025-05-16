package com.leverx.learningmanagementsystem.destination.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HttpHeader(
        @JsonProperty("key") String key,
        @JsonProperty("value") String value) {
}