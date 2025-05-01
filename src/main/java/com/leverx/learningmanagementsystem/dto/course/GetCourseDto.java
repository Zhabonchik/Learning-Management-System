package com.leverx.learningmanagementsystem.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record GetCourseDto(
        UUID id,
        @NotBlank(message = "Title must not be blank") String title,
        @NotBlank(message = "Description must not be blank") String description,
        @PositiveOrZero(message = "Price must be >= 0") BigDecimal price,
        @PositiveOrZero(message = "Coins paid must be >= 0") BigDecimal coinsPaid,
        @NotNull(message = "Course must have course settings") UUID courseSettingsId,
        List<UUID> lessonIds,
        List<UUID> studentIds) {
}
