package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CourseSettingsServiceImpl implements CourseSettingsService {

    private final CourseSettingsRepository courseSettingsRepository;
    private final CourseSettingsMapper courseSettingsMapper;

    @Autowired
    public CourseSettingsServiceImpl(CourseSettingsRepository courseSettingsRepository,
                                     CourseSettingsMapper courseSettingsMapper) {
        this.courseSettingsRepository = courseSettingsRepository;
        this.courseSettingsMapper = courseSettingsMapper;
    }

    @Override
    public GetCourseSettingsDto getById(UUID id) {
        log.info("Get course settings by id: {}", id);
        CourseSettings courseSettings = courseSettingsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course settings with id = " + id + " not found"));
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
    public GetCourseSettingsDto create(CreateCourseSettingsDto createCourseDto) {
        CourseSettings courseSettings = courseSettingsMapper.toCourseSettings(createCourseDto);

        log.info("Create course settings: {}", courseSettings);
        courseSettingsRepository.save(courseSettings);

        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public GetCourseSettingsDto update(UUID id, CreateCourseSettingsDto updateCourseDto) {

        if (courseSettingsRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Course settings with id = " + id + " not found");
        }

        CourseSettings courseSettings = courseSettingsMapper.toCourseSettings(updateCourseDto);
        courseSettings.setId(id);

        log.info("Update course settings: {}", courseSettings);
        courseSettingsRepository.save(courseSettings);

        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public void delete(UUID id) {
        log.info("Delete course settings by id: {}", id);
        courseSettingsRepository.deleteById(id);
    }
}
