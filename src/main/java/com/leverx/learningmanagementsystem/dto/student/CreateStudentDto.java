package com.leverx.learningmanagementsystem.dto.student;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Builder
public record CreateStudentDto(
        String firstName,
        String lastName,
        String email,
        LocalDate dateOfBirth,
        BigDecimal coins,
        Set<UUID> courseId) {
}
