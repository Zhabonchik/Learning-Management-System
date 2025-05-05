package com.leverx.learningmanagementsystem.webfacade.Impl;

import com.leverx.learningmanagementsystem.dto.coursesettings.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.mapper.coursesettings.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import com.leverx.learningmanagementsystem.webfacade.CourseSettingsWebFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class CourseSettingsWebFacadeImpl implements CourseSettingsWebFacade {

    private final CourseSettingsService courseSettingsService;
    private final CourseSettingsMapper courseSettingsMapper;

    @Override
    public List<CourseSettingsResponseDto> getAll() {
        List<CourseSettings> courseSettings = courseSettingsService.getAll();
        return courseSettingsMapper.toDtos(courseSettings);
    }

    @Override
    public CourseSettingsResponseDto getById(UUID id) {
        CourseSettings courseSettings = courseSettingsService.getById(id);
        return courseSettingsMapper.toDto(courseSettings);
    }

    @Override
    public CourseSettingsResponseDto create(CreateCourseSettingsDto createCourseSettingsDto) {
        CourseSettings courseSettings = courseSettingsMapper.toModel(createCourseSettingsDto);
        CourseSettings createdCourseSettings = courseSettingsService.create(courseSettings);
        return courseSettingsMapper.toDto(createdCourseSettings);
    }

    @Override
    public CourseSettingsResponseDto updateById(UUID id, CreateCourseSettingsDto createCourseSettingsDto) {
        CourseSettings courseSettings = courseSettingsMapper.toModel(createCourseSettingsDto);
        courseSettings.setId(id);

        CourseSettings updatedCourseSettings = courseSettingsService.updateById(courseSettings);
        return courseSettingsMapper.toDto(updatedCourseSettings);
    }

    @Override
    public void deleteById(UUID id) {
        courseSettingsService.deleteById(id);
    }
}
