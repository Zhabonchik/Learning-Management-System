package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CourseSettingsService {

    GetCourseSettingsDto getById(UUID id);

    List<GetCourseSettingsDto> getAllCourseSettings();

    GetCourseSettingsDto create(CreateCourseSettingsDto createCourseDto);

    GetCourseSettingsDto update(UUID id, CreateCourseSettingsDto updateCourseDto);

    void delete(UUID id);

}
