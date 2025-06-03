package com.leverx.learningmanagementsystem.coursesettings.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leverx.learningmanagementsystem.core.constants.DateFormatConstants;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CreateCourseSettingsDto(
        @NotNull(message = "Start date must not be null") @JsonFormat(pattern = DateFormatConstants.DATE_TIME_FORMAT)
        @FutureOrPresent(message = "Start date must not be in past") LocalDateTime startDate,
        @NotNull(message = "End date must not be null") @JsonFormat(pattern = DateFormatConstants.DATE_TIME_FORMAT)
        @FutureOrPresent(message = "End date must not be in past") LocalDateTime endDate,
        boolean isPublic) {
}
