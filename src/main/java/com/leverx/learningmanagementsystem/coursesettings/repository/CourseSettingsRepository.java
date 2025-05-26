package com.leverx.learningmanagementsystem.coursesettings.repository;

import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface CourseSettingsRepository extends CrudRepository<CourseSettings, UUID> {
    List<CourseSettings> findAll();

    Page<CourseSettings> findAll(Pageable pageable);
}
