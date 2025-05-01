package com.leverx.learningmanagementsystem.dto.student;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record GetStudentDto(
        UUID id,
        @NotBlank(message = "First name must not be blank") String firstName,
        @NotBlank(message = "Last name must not be blank") String lastName,
        @Email String email,
        @Past(message = "Date of birth must be in past") LocalDate dateOfBirth,
        @PositiveOrZero(message = "Coins must be >= 0") BigDecimal coins,
        List<UUID> courseIds) {
}
