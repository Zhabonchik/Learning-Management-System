package com.leverx.learningmanagementsystem.coursesettings.service;

import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsService {

    CourseSettings getById(UUID id);

    List<CourseSettings> getAll();

    Page<CourseSettings> getAll(Pageable pageable);

    CourseSettings create(CourseSettings courseSettings);

    CourseSettings updateById(CourseSettings courseSettings);

    void deleteById(UUID id);

}
