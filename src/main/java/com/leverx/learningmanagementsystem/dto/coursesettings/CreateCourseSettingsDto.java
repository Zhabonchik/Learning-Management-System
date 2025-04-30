package com.leverx.learningmanagementsystem.dto.coursesettings;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateCourseSettingsDto(
        @NotNull(message = "Start date must not be null") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @FutureOrPresent(message = "Start date must not be in past") LocalDateTime startDate,
        @NotNull(message = "End date must not be null") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @FutureOrPresent(message = "End date must not be in past") LocalDateTime endDate,
        boolean isPublic) {
}
