package com.leverx.learningmanagementsystem.student.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Builder
public record CreateStudentDto(
        @NotBlank(message = "First name must not be blank") String firstName,
        @NotBlank(message = "Last name must not be blank") String lastName,
        @Email String email,
        @Past(message = "Date of birth must be in past") LocalDate dateOfBirth,
        @PositiveOrZero(message = "Coins must be >= 0") BigDecimal coins,
        @NotNull(message = "Language must not be empty") Locale locale,
        @Nullable List<UUID> courseIds) {
}
