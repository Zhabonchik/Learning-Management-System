package com.leverx.learningmanagementsystem.lesson.video.dto;

import com.leverx.learningmanagementsystem.lesson.common.dto.LessonResponseDto;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.util.UUID;

@Builder
public record VideoLessonResponseDto(
        @NonNull UUID id,
        @NonNull String title,
        @NonNull Integer durationInMinutes,
        @Nullable UUID courseId,
        @NonNull String url,
        @NonNull String platform,
        @NonNull Instant created,
        @NonNull String createdBy,
        @Nullable Instant lastModified,
        @Nullable String lastModifiedBy) implements LessonResponseDto {
}
