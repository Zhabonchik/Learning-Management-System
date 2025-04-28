package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CourseService {

    GetCourseDto getById(UUID id);

    List<GetCourseDto> getAllCourses();

    GetCourseDto create(CreateCourseDto createCourseDto);

    GetCourseDto update(UUID id, CreateCourseDto updateCourseDto);

    void delete(UUID id);

}
