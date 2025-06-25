package com.leverx.learningmanagementsystem.lesson.common.facade;

import com.leverx.learningmanagementsystem.lesson.common.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.common.dto.LessonResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LessonWebFacade {

    List<LessonResponseDto> getAll();

    Page<LessonResponseDto> getAll(Pageable pageable);

    LessonResponseDto getById(UUID id);

    LessonResponseDto create(CreateLessonDto dto);

    LessonResponseDto updateById(UUID id, CreateLessonDto dto);

    void deleteById(UUID id);

}
