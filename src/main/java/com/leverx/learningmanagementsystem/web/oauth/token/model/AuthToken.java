package com.leverx.learningmanagementsystem.web.oauth.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leverx.learningmanagementsystem.web.http.model.HttpHeader;

public record AuthToken(
        @JsonProperty("type") String type,
        @JsonProperty("value") String value,
        @JsonProperty("http_header") HttpHeader httpHeader) {
}
