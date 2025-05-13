package com.leverx.learningmanagementsystem.course.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CourseResponseDto(
        @NonNull UUID id,
        @NonNull String title,
        @NonNull String description,
        @NonNull BigDecimal price,
        @NonNull BigDecimal coinsPaid,
        @Nullable UUID courseSettingsId,
        @Nullable List<UUID> lessonIds,
        @Nullable List<UUID> studentIds) {
}
