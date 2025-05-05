package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import com.leverx.learningmanagementsystem.utils.CourseSettingsValidator;
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
    private final CourseSettingsMapper courseSettingsMapper;

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
        CourseSettingsValidator.validateCourseDates(courseSettings);

        log.info("Create course settings: {}", courseSettings);
        courseSettingsRepository.save(courseSettings);

        return courseSettings;
    }

    @Override
    @Transactional
    public CourseSettings updateById(CourseSettings courseSettings) {
        if (courseSettingsRepository.findById(courseSettings.getId()).isEmpty()) {
            throw new EntityNotFoundException("Course settings not found [id = {%s}]".formatted(courseSettings.getId()));
        }

        CourseSettingsValidator.validateCourseDates(courseSettings);

        log.info("Update course settings: {}", courseSettings);
        courseSettingsRepository.save(courseSettings);

        return courseSettings;
    }

    @Override
    public void deleteById(UUID id) {
        getById(id);
        log.info("Delete course settings [id = {}]", id);
        courseSettingsRepository.deleteById(id);
    }
}
