package com.leverx.learningmanagementsystem.btp.destinationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Destination {

    @JsonProperty("owner")
    private Owner owner;

    @JsonProperty("destinationConfiguration")
    private DestinationConfiguration destinationConfiguration;

    @JsonProperty("authTokens")
    private List<DestinationAuthToken> authTokens;
}
