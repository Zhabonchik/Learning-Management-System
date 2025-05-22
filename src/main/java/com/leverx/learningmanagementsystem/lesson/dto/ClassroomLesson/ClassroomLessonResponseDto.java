package com.leverx.learningmanagementsystem.lesson.dto.ClassroomLesson;

import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record ClassroomLessonResponseDto(
        @NonNull UUID id,
        @NonNull String title,
        @NonNull Integer durationInMinutes,
        @Nullable UUID courseId,
        @NonNull String location,
        @NonNull Integer capacity) implements LessonResponseDto {
}
