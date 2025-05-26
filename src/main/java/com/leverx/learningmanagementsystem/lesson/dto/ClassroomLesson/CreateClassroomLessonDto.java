package com.leverx.learningmanagementsystem.lesson.dto.ClassroomLesson;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateClassroomLessonDto(
        @NotBlank(message = "Title must not be blank") String title,
        @NotNull(message = "Duration must not be null") @Min(value = 0, message = "Duration must be >= 0")
        @Max(value = 90, message = "Duration must be <= 90") Integer durationInMinutes,
        @Nullable UUID courseId,
        @NotBlank(message = "Location must not be empty") String location,
        @NotNull(message = "Capacity must not be empty") Integer capacity) implements CreateLessonDto {
}
