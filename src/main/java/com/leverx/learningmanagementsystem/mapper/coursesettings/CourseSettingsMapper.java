package com.leverx.learningmanagementsystem.mapper.coursesettings;

import com.leverx.learningmanagementsystem.dto.coursesettings.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
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
