package com.leverx.learningmanagementsystem.dto.course;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
public record GetCourseDto(
        UUID id,
        String title,
        String description,
        BigDecimal price,
        BigDecimal coinsPaid,
        UUID courseSettingsId,
        List<UUID> lessonId,
        List<UUID> studentId) {
}
