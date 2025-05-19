package com.leverx.learningmanagementsystem.coursesettings.webfacade;

import com.leverx.learningmanagementsystem.coursesettings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.coursesettings.mapper.CourseSettingsMapper;
import com.leverx.learningmanagementsystem.coursesettings.service.CourseSettingsService;
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
        var courseSettings = courseSettingsService.getById(id);
        return courseSettingsMapper.toDto(courseSettings);
    }

    @Override
    public CourseSettingsResponseDto create(CreateCourseSettingsDto createCourseSettingsDto) {
        var courseSettings = courseSettingsMapper.toModel(createCourseSettingsDto);
        var createdCourseSettings = courseSettingsService.create(courseSettings);
        return courseSettingsMapper.toDto(createdCourseSettings);
    }

    @Override
    public CourseSettingsResponseDto updateById(UUID id, CreateCourseSettingsDto createCourseSettingsDto) {
        var courseSettings = courseSettingsMapper.toModel(createCourseSettingsDto);
        courseSettings.setId(id);

        var updatedCourseSettings = courseSettingsService.updateById(courseSettings);
        return courseSettingsMapper.toDto(updatedCourseSettings);
    }

    @Override
    public void deleteById(UUID id) {
        courseSettingsService.deleteById(id);
    }
}
