package com.leverx.learningmanagementsystem.coursesettings.service;

import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
import com.leverx.learningmanagementsystem.coursesettings.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.utils.validator.CourseSettingsValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CourseSettingsServiceImpl implements CourseSettingsService {

    private final CourseSettingsRepository courseSettingsRepository;
    private final CourseSettingsValidator courseSettingsValidator;

    @Override
    public CourseSettings getById(UUID id) {
        log.info("Get course settings [id = {}]", id);
        return courseSettingsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course settings not found [id = {%s}]".formatted(id)));
    }

    @Override
    public List<CourseSettings> getAll() {
        log.info("Get all course settings");
        return courseSettingsRepository.findAll();
    }

    @Override
    public CourseSettings create(CourseSettings courseSettings) {
        courseSettingsValidator.validateCourseDates(courseSettings);

        log.info("Create course settings: {}", courseSettings);
        return courseSettingsRepository.save(courseSettings);
    }

    @Override
    @Transactional
    public CourseSettings updateById(CourseSettings courseSettings) {
        if (courseSettingsRepository.findById(courseSettings.getId()).isEmpty()) {
            throw new EntityNotFoundException("Course settings not found [id = {%s}]".formatted(courseSettings.getId()));
        }

        courseSettingsValidator.validateCourseDates(courseSettings);

        log.info("Update course settings: {}", courseSettings);
        return courseSettingsRepository.save(courseSettings);
    }

    @Override
    public void deleteById(UUID id) {
        getById(id);
        log.info("Delete course settings [id = {}]", id);
        courseSettingsRepository.deleteById(id);
    }
}
