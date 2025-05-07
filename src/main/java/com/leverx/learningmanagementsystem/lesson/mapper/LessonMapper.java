package com.leverx.learningmanagementsystem.lesson.mapper;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", expression = "java(lesson.getCourse().getId())")
    LessonResponseDto toDto(Lesson lesson);

    @Mapping(target = "course", ignore = true)
    Lesson toModel(LessonResponseDto lessonDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    Lesson toModel(CreateLessonDto createLessonDto);

    List<LessonResponseDto> toDtos(List<Lesson> lessons);
}
