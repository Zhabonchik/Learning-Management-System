package com.leverx.learningmanagementsystem.coursesettings.service;

import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsService {

    CourseSettings getById(UUID id);

    List<CourseSettings> getAll();

    CourseSettings create(CourseSettings courseSettings);

    CourseSettings updateById(CourseSettings courseSettings);

    void deleteById(UUID id);

}
