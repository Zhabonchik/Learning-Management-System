package com.leverx.learningmanagementsystem.webfacade;

import com.leverx.learningmanagementsystem.dto.coursesettings.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsWebFacade {

    List<CourseSettingsResponseDto> getAll();

    CourseSettingsResponseDto getById(UUID id);

    CourseSettingsResponseDto create(CreateCourseSettingsDto createCourseSettingsDto);

    CourseSettingsResponseDto updateById(UUID id, CreateCourseSettingsDto createCourseSettingsDto);

    void deleteById(UUID id);

}
