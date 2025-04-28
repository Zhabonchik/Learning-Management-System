package com.leverx.learningmanagementsystem.dto.coursesettings;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateCourseSettingsDto(
        LocalDateTime startDate,
        LocalDateTime endDate,
        boolean isPublic
) {
}
