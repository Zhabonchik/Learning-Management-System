package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.entity.Lesson;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface LessonService {

    Lesson getById(UUID id);

    List<Lesson> getAll();

    Lesson create(CreateLessonDto createLessonDto);

    Lesson update(UUID id, CreateLessonDto updateLessonDto);

    void delete(UUID id);
}
