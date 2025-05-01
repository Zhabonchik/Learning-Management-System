package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.InvalidCourseDatesException;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.EntityNotFoundException;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.leverx.learningmanagementsystem.utils.DataFormatUtils.DATE_TIME_FORMAT;

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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course settings with id = " + id + " not found"));
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public List<GetCourseSettingsDto> getAllCourseSettings() {
        log.info("Get all course settings");
        return courseSettingsMapper.toGetCourseSettingsDtoList(courseSettingsRepository.findAll());
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
            throw new EntityNotFoundException("Course settings with id = " + id + " not found");
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
            throw new InvalidCourseDatesException(
                    "Invalid date format, expected format: " + DATE_TIME_FORMAT);
        }

        if (courseSettings.getStartDate().isAfter(courseSettings.getEndDate())) {
            throw new InvalidCourseDatesException(
                    "Course start date is after course end date");
        }
    }
}
