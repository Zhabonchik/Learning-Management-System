package com.leverx.learningmanagementsystem.lesson.common.dto.impl;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record LessonResponseDto(
        @NonNull UUID id,
        @NonNull String title,
        @NonNull Integer durationInMinutes,
        @Nullable UUID courseId) {
}
