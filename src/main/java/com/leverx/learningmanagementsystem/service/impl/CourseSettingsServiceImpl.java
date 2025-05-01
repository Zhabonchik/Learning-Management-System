package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.exception.EntityValidationException;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
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
    public GetCourseSettingsDto getById(UUID id) {
        log.info("Get course settings by id: {}", id);
        CourseSettings courseSettings = courseSettingsRepository.findById(id)
                .orElseThrow(() -> new EntityValidationException.EntityNotFoundException(
                        "Course settings with id = " + id + " not found"));
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public List<GetCourseSettingsDto> getAllCourseSettings() {
        log.info("Get all course settings");
        List<CourseSettings> courseSettingsList = (List<CourseSettings>) courseSettingsRepository.findAll();
        return courseSettingsList.stream()
                .map(courseSettingsMapper::toGetCourseSettingsDto)
                .toList();
    }

    @Override
    @Transactional
    public GetCourseSettingsDto create(CreateCourseSettingsDto createCourseDto) {
        CourseSettings courseSettings = courseSettingsMapper.toCourseSettings(createCourseDto);

        checkDate(courseSettings);

        log.info("Create course settings: {}", courseSettings);
        courseSettingsRepository.save(courseSettings);

        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    @Transactional
    public GetCourseSettingsDto update(UUID id, CreateCourseSettingsDto updateCourseDto) {

        if (courseSettingsRepository.findById(id).isEmpty()) {
            throw new EntityValidationException.EntityNotFoundException("Course settings with id = " + id + " not found");
        }

        CourseSettings courseSettings = courseSettingsMapper.toCourseSettings(updateCourseDto);
        courseSettings.setId(id);

        checkDate(courseSettings);

        log.info("Update course settings: {}", courseSettings);
        courseSettingsRepository.save(courseSettings);

        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public void delete(UUID id) {
        log.info("Delete course settings by id: {}", id);
        courseSettingsRepository.deleteById(id);
    }

    private void checkDate(CourseSettings courseSettings) {

        if (courseSettings.getStartDate() == null || courseSettings.getEndDate() == null) {
            throw new EntityValidationException.InvalidCourseDatesException(
                    "Invalid date format, expected format: yyyy-MM-dd HH:mm:ss");
        }

        if (courseSettings.getStartDate().isAfter(courseSettings.getEndDate())) {
            throw new EntityValidationException.InvalidCourseDatesException(
                    "Course start date is after course end date");
        }
    }
}
