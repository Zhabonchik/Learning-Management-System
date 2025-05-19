package com.leverx.learningmanagementsystem.student.dto;

import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record StudentResponseDto(
        @NonNull UUID id,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String email,
        @NonNull LocalDate dateOfBirth,
        @NonNull BigDecimal coins,
        @NonNull List<UUID> courseIds) {
}
