package com.leverx.learningmanagementsystem.course.mapper;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.student.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "courseSettingsId", source = "settings", qualifiedByName = "mapToSettingsId")
    @Mapping(target = "lessonIds", source = "lessons", qualifiedByName = "mapToLessonIds")
    @Mapping(target = "studentIds", source = "students", qualifiedByName = "mapToStudentIds")
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

    @Named("mapToSettingsId")
    default UUID mapToSettingsId(CourseSettings courseSettings) {
        return (isNull(courseSettings)) ? null : courseSettings.getId();
    }

    @Named("mapToLessonIds")
    default List<UUID> mapToLessonIds(List<Lesson> lessons) {
        return lessons.stream()
                .map(Lesson::getId)
                .toList();
    }

    @Named("mapToStudentIds")
    default List<UUID> mapToStudentIds(List<Student> students) {
        return students.stream()
                .map(Student::getId)
                .toList();
    }
}
