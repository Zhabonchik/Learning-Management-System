package com.leverx.learningmanagementsystem.btp.featureflagservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Profile;

@Profile("cloud")
public record FeatureFlagsResponse (
        @NotNull Integer httpStatus,
        @NotEmpty String featureName,
        @NotEmpty String type,
        @NotNull Boolean variation ){
}
