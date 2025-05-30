package com.leverx.learningmanagementsystem.cloud.destinationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leverx.learningmanagementsystem.web.oauth.token.model.AuthToken;
import lombok.Data;

import java.util.List;

@Data
public class Destination {

    @JsonProperty("owner")
    private Owner owner;

    @JsonProperty("destinationConfiguration")
    private DestinationConfiguration destinationConfiguration;

    @JsonProperty("authTokens")
    private List<AuthToken> authTokens;
}
