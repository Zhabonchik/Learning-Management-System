package com.leverx.learningmanagementsystem.mapper.lesson;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.GetLessonDto;
import com.leverx.learningmanagementsystem.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", expression = "java(lesson.getCourse().getId())")
    GetLessonDto toGetLessonDto(Lesson lesson);

    @Mapping(target = "course", ignore = true)
    Lesson toLesson(GetLessonDto lessonDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "course", ignore = true)
    Lesson toLesson(CreateLessonDto createLessonDto);
}
