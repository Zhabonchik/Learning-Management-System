package com.leverx.learningmanagementsystem.course.webfacade;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;

import java.util.List;
import java.util.UUID;

public interface CourseWebFacade {

    List<CourseResponseDto> getAll();

    CourseResponseDto getById(UUID id);

    CourseResponseDto create(CreateCourseDto createCourseDto);

    CourseResponseDto updateById(UUID id, CreateCourseDto createCourseDto);

    void deleteById(UUID id);

}
