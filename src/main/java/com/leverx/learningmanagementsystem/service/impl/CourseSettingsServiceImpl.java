package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        CourseSettings courseSettings = courseSettingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course settings not found"));
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public List<GetCourseSettingsDto> getAllCourseSettings() {
        List<CourseSettings> courseSettingsList = (List<CourseSettings>) courseSettingsRepository.findAll();
        return courseSettingsList.stream()
                .map(courseSettingsMapper::toGetCourseSettingsDto)
                .toList();
    }

    @Override
    public GetCourseSettingsDto create(CreateCourseSettingsDto createCourseDto) {
        CourseSettings courseSettings = courseSettingsMapper.toCourseSettings(createCourseDto);
        courseSettingsRepository.save(courseSettings);
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public GetCourseSettingsDto update(UUID id, CreateCourseSettingsDto updateCourseDto) {

        if (courseSettingsRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Course settings not found");
        }

        CourseSettings courseSettings = courseSettingsMapper.toCourseSettings(updateCourseDto);
        courseSettings.setId(id);
        courseSettingsRepository.save(courseSettings);
        return courseSettingsMapper.toGetCourseSettingsDto(courseSettings);
    }

    @Override
    public void delete(UUID id) {
        courseSettingsRepository.deleteById(id);
    }
}
