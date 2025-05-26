package com.leverx.learningmanagementsystem.lesson.service;

import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    Lesson getById(UUID id);

    List<Lesson> getAll();

    Page<Lesson> getAll(Pageable pageable);

    List<Lesson> getAllByIdIn(List<UUID> ids);

    Lesson create(Lesson lesson);

    Lesson updateById(Lesson lesson);

    void deleteById(UUID id);
}
