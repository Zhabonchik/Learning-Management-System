package com.leverx.learningmanagementsystem.webfacade;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.LessonResponseDto;

import java.util.List;
import java.util.UUID;

public interface LessonWebFacade {

    List<LessonResponseDto> getAll();

    LessonResponseDto getById(UUID id);

    LessonResponseDto create(CreateLessonDto dto);

    LessonResponseDto updateById(UUID id, CreateLessonDto dto);

    void deleteById(UUID id);

}
