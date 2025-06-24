package com.leverx.learningmanagementsystem.course.facade;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CourseWebFacade {

    List<CourseResponseDto> getAll();

    Page<CourseResponseDto> getAll(Pageable pageable);

    CourseResponseDto getById(UUID id);

    CourseResponseDto create(CreateCourseDto createCourseDto);

    CourseResponseDto updateById(UUID id, CreateCourseDto createCourseDto);

    void deleteById(UUID id);

}
