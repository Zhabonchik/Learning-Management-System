package com.leverx.learningmanagementsystem.lesson.dto.VideoLesson;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonType;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateVideoLessonDto(
        @NotBlank(message = "Title must not be blank") String title,
        @NotNull(message = "Duration must not be null") @Min(value = 0, message = "Duration must be >= 0")
        @Max(value = 90, message = "Duration must be <= 90") Integer durationInMinutes,
        @Nullable UUID courseId,
        @NotBlank(message = "Url must not be empty") String url,
        @NotBlank(message = "Platform must not be empty") String platform,
        @NotNull(message = "Type must not be blank") LessonType type) implements CreateLessonDto {
}
