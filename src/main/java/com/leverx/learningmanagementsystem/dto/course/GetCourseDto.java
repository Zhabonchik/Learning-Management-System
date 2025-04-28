package com.leverx.learningmanagementsystem.dto.course;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Builder
public record GetCourseDto(
        UUID id,
        String title,
        String description,
        BigDecimal price,
        BigDecimal coinsPaid,
        UUID courseSettingsId,
        Set<UUID> lessonId,
        Set<UUID> studentId
) {
}
