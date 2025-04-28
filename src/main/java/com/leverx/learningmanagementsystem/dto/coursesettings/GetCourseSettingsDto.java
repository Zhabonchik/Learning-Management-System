package com.leverx.learningmanagementsystem.dto.coursesettings;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record GetCourseSettingsDto(
        UUID id,
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isPublic
) {
}
