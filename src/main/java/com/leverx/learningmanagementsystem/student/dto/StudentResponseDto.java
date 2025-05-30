package com.leverx.learningmanagementsystem.student.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Builder
public record StudentResponseDto(
        @NonNull UUID id,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String email,
        @NonNull LocalDate dateOfBirth,
        @NonNull BigDecimal coins,
        @NonNull Locale locale,
        @NonNull Instant created,
        @NonNull String createdBy,
        @Nullable Instant lastModified,
        @Nullable String lastModifiedBy,
        @NonNull List<UUID> courseIds) {
}
