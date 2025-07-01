package com.leverx.learningmanagementsystem.btp.destinationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DestinationRequest {

    private String uri;
    private String tokenUrl;
    private String clientId;
    private String clientSecret;

    public static DestinationRequest of(String uri, String tokenUrl, String clientId, String clientSecret) {
        return new DestinationRequest(uri, tokenUrl, clientId, clientSecret);
    }
}
