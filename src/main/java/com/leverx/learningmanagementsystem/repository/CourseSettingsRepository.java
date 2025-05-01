package com.leverx.learningmanagementsystem.repository;

import com.leverx.learningmanagementsystem.entity.CourseSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseSettingsRepository extends CrudRepository<CourseSettings, UUID> {
    List<CourseSettings> findAll();
}
