package com.leverx.learningmanagementsystem.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leverx.learningmanagementsystem.destination.model.HttpHeader;

public record AuthToken(
        @JsonProperty("type") String type,
        @JsonProperty("value") String value,
        @JsonProperty("http_header") HttpHeader httpHeader) {
}
