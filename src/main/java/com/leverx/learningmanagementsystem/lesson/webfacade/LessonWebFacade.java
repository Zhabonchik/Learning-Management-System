package com.leverx.learningmanagementsystem.lesson.webfacade;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;

import java.util.List;
import java.util.UUID;

public interface LessonWebFacade {

    List<LessonResponseDto> getAll();

    LessonResponseDto getById(UUID id);

    LessonResponseDto create(CreateLessonDto dto);

    LessonResponseDto updateById(UUID id, CreateLessonDto dto);

    void deleteById(UUID id);

}
