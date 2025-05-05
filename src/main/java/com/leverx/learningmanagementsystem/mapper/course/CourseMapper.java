package com.leverx.learningmanagementsystem.mapper.course;

import com.leverx.learningmanagementsystem.dto.course.CourseResponseDto;
import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "courseSettingsId", expression = "java(course.getSettings() != null ? course.getSettings().getId() : null)")
    @Mapping(target = "lessonIds",
            expression = "java(course.getLessons().stream()"
                    + ".map(com.leverx.learningmanagementsystem.entity.Lesson::getId)"
                    + ".collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "studentIds",
            expression = "java(course.getStudents().stream()"
                    + ".map(com.leverx.learningmanagementsystem.entity.Student::getId)"
                    + ".collect(java.util.stream.Collectors.toList()))")
    CourseResponseDto toDto(Course course);

    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "students", ignore = true)
    Course toModel(CourseResponseDto courseResponseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "students", ignore = true)
    Course toModel(CreateCourseDto createCourseDto);

    List<CourseResponseDto> toDtos(List<Course> courses);

}
