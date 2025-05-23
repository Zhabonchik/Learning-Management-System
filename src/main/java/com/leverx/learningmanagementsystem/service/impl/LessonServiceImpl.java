package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.IncorrectResultSizeException;
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
    public List<Lesson> getAllByIdIn(List<UUID> ids) {
        List<Lesson> lessons = lessonRepository.findAllByIdIn(ids);
        if (lessons.size() != ids.size()) {
            throw new IncorrectResultSizeException("Some of requested lessons don't exist");
        }
        return lessons;
    }

    @Override
    @Transactional
    public Lesson create(Lesson lesson) {
        log.info("Create lesson: {}", lesson);
        return lessonRepository.save(lesson);
    }

    @Override
    @Transactional
    public Lesson updateById(Lesson lesson) {
        if (lessonRepository.findById(lesson.getId()).isEmpty()) {
            throw new EntityNotFoundException("Lesson not found [id = {%s}]".formatted(lesson.getId()));
        }
        log.info("Update lesson [id = {}]", lesson.getId());
        return lessonRepository.save(lesson);
    }

    @Override
    public void deleteById(UUID id) {
        getById(id);
        log.info("Delete Lesson [id = {}]", id);
        lessonRepository.deleteById(id);
    }
}
