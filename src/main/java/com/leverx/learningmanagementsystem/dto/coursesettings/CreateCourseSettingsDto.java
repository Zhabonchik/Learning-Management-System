package com.leverx.learningmanagementsystem.dto.coursesettings;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leverx.learningmanagementsystem.utils.DataFormatUtils;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateCourseSettingsDto(
        @NotNull(message = "Start date must not be null") @JsonFormat(pattern = DataFormatUtils.DATE_TIME_FORMAT)
        @FutureOrPresent(message = "Start date must not be in past") LocalDateTime startDate,
        @NotNull(message = "End date must not be null") @JsonFormat(pattern = DataFormatUtils.DATE_TIME_FORMAT)
        @FutureOrPresent(message = "End date must not be in past") LocalDateTime endDate,
        boolean isPublic) {
}
