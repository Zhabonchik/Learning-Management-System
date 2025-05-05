package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CourseService {

    Course getById(UUID id);

    List<Course> getAll();

    Course create(CreateCourseDto createCourseDto);

    Course updateById(UUID id, CreateCourseDto updateCourseDto);

    List<Course> getAllStartingBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    void deleteById(UUID id);

}
