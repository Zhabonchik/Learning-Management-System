package com.leverx.learningmanagementsystem.course.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record CreateCourseDto(
        @NotBlank(message = "Title must not be blank") String title,
        @NotBlank(message = "Description must not be blank") String description,
        @PositiveOrZero(message = "Price must be >= 0") BigDecimal price,
        @PositiveOrZero(message = "Coins paid must be >= 0") BigDecimal coinsPaid,
        @Nullable UUID courseSettingsId,
        @Nullable List<UUID> lessonIds,
        @Nullable List<UUID> studentIds) {
}
