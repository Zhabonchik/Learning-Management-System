package com.leverx.learningmanagementsystem.mapper.lesson;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.LessonResponseDto;
import com.leverx.learningmanagementsystem.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", expression = "java(lesson.getCourse().getId())")
    LessonResponseDto toDto(Lesson lesson);

    @Mapping(target = "course", ignore = true)
    Lesson toLesson(LessonResponseDto lessonDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    Lesson toLesson(CreateLessonDto createLessonDto);

    List<LessonResponseDto> toDtoList(List<Lesson> lessons);
}
