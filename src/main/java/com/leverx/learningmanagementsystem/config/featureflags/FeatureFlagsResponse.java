package com.leverx.learningmanagementsystem.config.featureflags;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record FeatureFlagsResponse (
        @NotNull Integer httpStatus,
        @NotEmpty String featureName,
        @NotEmpty String type,
        @NotNull Boolean variation ){
}
