package com.leverx.learningmanagementsystem.course.dto;

import lombok.NonNull;

import java.util.UUID;

public record CourseId(@NonNull UUID id) {
    public static CourseId of(@NonNull UUID id) {
        return new CourseId(id);
    }
}
