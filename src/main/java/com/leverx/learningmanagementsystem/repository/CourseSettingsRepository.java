package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.CourseSettings;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsRepository extends CrudRepository<CourseSettings, UUID> {
    List<CourseSettings> findAll();
}
