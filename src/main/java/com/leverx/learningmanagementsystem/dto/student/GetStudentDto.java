package com.leverx.learningmanagementsystem.dto.student;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record GetStudentDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        LocalDate dateOfBirth,
        BigDecimal coins,
        List<UUID> courseId) {
}
