package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CourseSettingsService {

    CourseSettings getById(UUID id);

    List<CourseSettings> getAll();

    CourseSettings create(CreateCourseSettingsDto createCourseDto);

    CourseSettings update(UUID id, CreateCourseSettingsDto updateCourseDto);

    void delete(UUID id);

}
