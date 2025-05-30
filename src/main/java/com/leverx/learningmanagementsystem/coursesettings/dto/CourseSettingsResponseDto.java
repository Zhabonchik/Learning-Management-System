package com.leverx.learningmanagementsystem.coursesettings.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.leverx.learningmanagementsystem.core.utils.DataFormatUtils;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CourseSettingsResponseDto(
        @NonNull UUID id,
        @NonNull @JsonFormat(pattern = DataFormatUtils.DATE_TIME_FORMAT) LocalDateTime startDate,
        @NonNull @JsonFormat(pattern = DataFormatUtils.DATE_TIME_FORMAT) LocalDateTime endDate,
        boolean isPublic,
        @NonNull Instant created,
        @NonNull String createdBy,
        @Nullable Instant lastModified,
        @Nullable String lastModifiedBy) {
}
