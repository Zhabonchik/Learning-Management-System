package com.leverx.learningmanagementsystem.course_settings.mapper;

import com.leverx.learningmanagementsystem.course_settings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.course_settings.dto.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.course_settings.model.CourseSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseSettingsMapper {

    CourseSettingsResponseDto toDto(CourseSettings courseSettings);

    CourseSettings toModel(CourseSettingsResponseDto courseSettingsResponseDto);

    @Mapping(target = "id", ignore = true)
    CourseSettings toModel(CreateCourseSettingsDto createCourseSettingsDto);

    List<CourseSettingsResponseDto> toDtos(List<CourseSettings> courseSettingsList);
}
