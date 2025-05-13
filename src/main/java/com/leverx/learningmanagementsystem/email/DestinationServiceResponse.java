package com.leverx.learningmanagementsystem.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DestinationServiceResponse {
    @JsonProperty("destinationConfiguration")
    private DestinationServiceMailConfig destinationConfiguration;
}
