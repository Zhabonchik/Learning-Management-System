package com.leverx.learningmanagementsystem.course_settings.webfacade;

import com.leverx.learningmanagementsystem.course_settings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.course_settings.dto.CreateCourseSettingsDto;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsWebFacade {

    List<CourseSettingsResponseDto> getAll();

    CourseSettingsResponseDto getById(UUID id);

    CourseSettingsResponseDto create(CreateCourseSettingsDto createCourseSettingsDto);

    CourseSettingsResponseDto updateById(UUID id, CreateCourseSettingsDto createCourseSettingsDto);

    void deleteById(UUID id);

}
