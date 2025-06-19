package com.leverx.learningmanagementsystem.btp.servicemanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record BindResource (
        @JsonProperty("app_guid") String appGuid,
        @JsonProperty("space_guid") String spaceGuid
) {
}
