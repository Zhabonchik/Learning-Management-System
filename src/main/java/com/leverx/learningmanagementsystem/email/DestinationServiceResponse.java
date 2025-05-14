package com.leverx.learningmanagementsystem.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.leverx.learningmanagementsystem.email.mailconfig.Destination;
import lombok.Data;

@Data
public class DestinationServiceResponse {
    @JsonProperty("destinationConfiguration")
    private Destination destinationConfiguration;
}
