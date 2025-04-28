package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.GetLessonDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface LessonService {

    GetLessonDto getById(UUID id);

    List<GetLessonDto> getAllLessons();

    GetLessonDto create(CreateLessonDto createLessonDto);

    GetLessonDto update(UUID id, CreateLessonDto updateLessonDto);

    void delete(UUID id);
}
