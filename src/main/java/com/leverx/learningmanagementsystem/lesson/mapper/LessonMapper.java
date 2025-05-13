package com.leverx.learningmanagementsystem.lesson.mapper;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", source= "course", qualifiedByName = "mapToCourseId")
    LessonResponseDto toDto(Lesson lesson);

    @Mapping(target = "course", ignore = true)
    Lesson toModel(LessonResponseDto lessonDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    Lesson toModel(CreateLessonDto createLessonDto);

    List<LessonResponseDto> toDtos(List<Lesson> lessons);

    @Named("mapToCourseId")
    default UUID mapToCourseId(Course course) {
        return (Objects.isNull(course)) ? null : course.getId();
    }
}
