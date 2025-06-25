package com.leverx.featureflagsstarter.btp.featureflagservice.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record FeatureFlagsResponse (
        @NotNull Integer httpStatus,
        @NotEmpty String featureName,
        @NotEmpty String type,
        @NotNull Boolean variation ){
}
