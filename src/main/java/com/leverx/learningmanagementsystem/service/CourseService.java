package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.entity.Course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CourseService {

    Course getById(UUID id);

    List<Course> getAll();

    List<Course> getAllByIdIn(List<UUID> ids);

    Course create(Course course);

    Course update(Course course);

    List<Course> getAllStartingBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    void deleteById(UUID id);

}
