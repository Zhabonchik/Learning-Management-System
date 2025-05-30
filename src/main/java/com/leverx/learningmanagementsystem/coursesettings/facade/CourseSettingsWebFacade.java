package com.leverx.learningmanagementsystem.coursesettings.facade;

import com.leverx.learningmanagementsystem.coursesettings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsWebFacade {

    List<CourseSettingsResponseDto> getAll();

    Page<CourseSettingsResponseDto> getAll(Pageable pageable);

    CourseSettingsResponseDto getById(UUID id);

    CourseSettingsResponseDto create(CreateCourseSettingsDto createCourseSettingsDto);

    CourseSettingsResponseDto updateById(UUID id, CreateCourseSettingsDto createCourseSettingsDto);

    void deleteById(UUID id);

}
