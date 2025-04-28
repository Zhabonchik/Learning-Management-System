package com.leverx.learningmanagementsystem.dto.lesson;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateLessonDto(
        String title,
        Integer duration,
        UUID courseId) {
}
