package com.leverx.learningmanagementsystem.student.dto;

import lombok.NonNull;

import java.util.UUID;

public record StudentId(@NonNull UUID id) {

    public static StudentId of(@NonNull UUID id) {
        return new StudentId(id);
    }
}
