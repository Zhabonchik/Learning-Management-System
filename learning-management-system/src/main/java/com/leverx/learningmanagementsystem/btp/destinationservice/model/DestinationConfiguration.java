package com.leverx.learningmanagementsystem.btp.destinationservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "Type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MailDestinationConfiguration.class, name = "MAIL")
})
@Getter
@Setter
public abstract class DestinationConfiguration {

    @JsonProperty("type")
    protected String type;

    @JsonProperty("name")
    protected String name;
}
