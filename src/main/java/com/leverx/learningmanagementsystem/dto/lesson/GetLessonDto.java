package com.leverx.learningmanagementsystem.dto.lesson;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetLessonDto(
        UUID id,
        String title,
        Integer duration,
        UUID courseId
) {
}
