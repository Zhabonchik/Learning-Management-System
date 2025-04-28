package com.leverx.learningmanagementsystem.mapper.course;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "courseSettingsId", source = "settings.id")
    @Mapping(target = "lessonId",
            expression = "java(course.getLessons().stream()"
                    + ".map(com.leverx.learningmanagementsystem.entity.Lesson::getId)"
                    + ".collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "studentId",
            expression = "java(course.getStudents().stream()"
                    + ".map(com.leverx.learningmanagementsystem.entity.Student::getId)"
                    + ".collect(java.util.stream.Collectors.toSet()))")
    GetCourseDto toGetCourseDto(Course course);

    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "students", ignore = true)
    Course toCourse(GetCourseDto getCourseDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "settings", ignore = true)
    @Mapping(target = "lessons", ignore = true)
    @Mapping(target = "students", ignore = true)
    Course toCourse(CreateCourseDto getCourseDto);

}
