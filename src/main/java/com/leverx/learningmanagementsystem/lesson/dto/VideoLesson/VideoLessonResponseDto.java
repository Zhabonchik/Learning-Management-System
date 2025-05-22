package com.leverx.learningmanagementsystem.lesson.dto.VideoLesson;

import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record VideoLessonResponseDto(
        @NonNull UUID id,
        @NonNull String title,
        @NonNull Integer durationInMinutes,
        @Nullable UUID courseId,
        @NonNull String url,
        @NonNull String platform) implements LessonResponseDto {
}
