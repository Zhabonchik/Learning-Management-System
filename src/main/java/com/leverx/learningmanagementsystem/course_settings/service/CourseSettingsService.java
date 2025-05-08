package com.leverx.learningmanagementsystem.course_settings.service;

import com.leverx.learningmanagementsystem.course_settings.model.CourseSettings;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsService {

    CourseSettings getById(UUID id);

    List<CourseSettings> getAll();

    CourseSettings create(CourseSettings courseSettings);

    CourseSettings updateById(CourseSettings courseSettings);

    void deleteById(UUID id);

}
