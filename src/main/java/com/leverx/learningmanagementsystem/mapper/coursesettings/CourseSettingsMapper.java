package com.leverx.learningmanagementsystem.mapper.coursesettings;

import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import com.leverx.learningmanagementsystem.dto.coursesettings.GetCourseSettingsDto;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseSettingsMapper {

    GetCourseSettingsDto toGetCourseSettingsDto(CourseSettings courseSettings);

    CourseSettings toCourseSettings(GetCourseSettingsDto getCourseSettingsDto);

    @Mapping(target = "id", ignore = true)
    CourseSettings toCourseSettings(CreateCourseSettingsDto createCourseSettingsDto);

    List<GetCourseSettingsDto> toGetCourseSettingsDtoList(List<CourseSettings> courseSettingsList);
}
