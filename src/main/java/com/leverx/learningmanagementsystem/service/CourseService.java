package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public interface CourseService {

    GetCourseDto getById(UUID id);

    List<GetCourseDto> getAllCourses();

    GetCourseDto create(CreateCourseDto createCourseDto);

    GetCourseDto update(UUID id, CreateCourseDto updateCourseDto);

    List<Course> getAllStartingBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    void delete(UUID id);

}
