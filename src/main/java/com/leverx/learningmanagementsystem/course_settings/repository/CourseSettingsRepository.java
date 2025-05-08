package com.leverx.learningmanagementsystem.course_settings.repository;

import com.leverx.learningmanagementsystem.course_settings.model.CourseSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsRepository extends CrudRepository<CourseSettings, UUID> {
    List<CourseSettings> findAll();
}
