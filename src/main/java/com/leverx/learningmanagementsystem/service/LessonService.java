package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.entity.Lesson;

import java.util.List;
import java.util.UUID;

public interface LessonService {

    Lesson getById(UUID id);

    List<Lesson> getAll();

    Lesson create(Lesson lesson);

    Lesson updateById(Lesson lesson);

    void deleteById(UUID id);
}
