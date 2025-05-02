package com.leverx.learningmanagementsystem.dto.lesson;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.UUID;

@Builder
public record GetLessonDto(
        UUID id,
        @NotBlank(message = "Title must not be blank") String title,
        @NotNull(message = "Duration must not be null") @Min(value = 0, message = "Duration must be >= 0")
        @Max(value = 90, message = "Duration must be <= 90") Integer durationInMinutes,
        @NotNull(message = "Course id must not be null") UUID courseId) {
}
