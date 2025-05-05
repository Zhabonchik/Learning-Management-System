package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.mapper.lesson.LessonMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.LessonRepository;
import com.leverx.learningmanagementsystem.service.LessonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Override
    public Lesson getById(UUID id) {
        log.info("Get Lesson [id = {}]", id);
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Lesson not found [id = {%s}]".formatted(id)));
    }

    @Override
    public List<Lesson> getAll() {
        log.info("Get all Lessons");
        return lessonRepository.findAll();
    }

    @Override
    @Transactional
    public Lesson create(Lesson lesson) {
        log.info("Create lesson: {}", lesson);
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    @Transactional
    public Lesson updateById(Lesson lesson) {
        if (lessonRepository.findById(lesson.getId()).isEmpty()) {
            throw new EntityNotFoundException("Lesson not found [id = {%s}]".formatted(lesson.getId()));
        }
        log.info("Update lesson [id = {}]", lesson.getId());
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    public void deleteById(UUID id) {
        getById(id);
        log.info("Delete Lesson [id = {}]", id);
        lessonRepository.deleteById(id);
    }
}
