package com.leverx.learningmanagementsystem.web.http.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HttpHeader(
        @JsonProperty("key") String key,
        @JsonProperty("value") String value) {
}