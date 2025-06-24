package com.leverx.learningmanagementsystem.lesson.common.dto.impl;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.util.UUID;

@Builder
public record CreateLessonDto(
        @NotBlank(message = "Title must not be blank") String title,
        @NotNull(message = "Duration must not be null") @Min(value = 0, message = "Duration must be >= 0")
        @Max(value = 90, message = "Duration must be <= 90") Integer durationInMinutes,
        @Nullable UUID courseId) {
}
