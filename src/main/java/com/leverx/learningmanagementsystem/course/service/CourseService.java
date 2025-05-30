package com.leverx.learningmanagementsystem.course.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CourseService {

    Course getById(UUID id);

    Course getByIdForUpdate(UUID id);

    List<Course> getAll();

    Page<Course> getAll(Pageable pageable);

    List<Course> getAllByIdIn(List<UUID> ids);

    Course create(Course course);

    Course update(Course course);

    List<Course> getAllStartingBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    void deleteById(UUID id);

}
