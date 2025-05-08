package com.leverx.learningmanagementsystem.course.mapper;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "courseSettingsId", expression = "java(course.getSettings() != null ? course.getSettings().getId() : null)")
    @Mapping(target = "lessonIds",
            expression = "java(course.getLessons().stream()"
                    + ".map(com.leverx.learningmanagementsystem.lesson.model.Lesson::getId)"
                    + ".collect(java.util.stream.Collectors.toList()))")
    @Mapping(target = "studentIds",
            expression = "java(course.getStudents().stream()"
                    + ".map(com.leverx.learningmanagementsystem.student.model.Student::getId)"
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
