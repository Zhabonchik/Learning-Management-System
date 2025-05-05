package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.entity.Course;
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
        log.info("Get Lesson by id: {}", id);
        return lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Lesson with id = " + id + " not found"));
    }

    @Override
    public List<Lesson> getAll() {
        log.info("Get all Lessons");
        return lessonRepository.findAll();
    }

    @Override
    @Transactional
    public Lesson create(CreateLessonDto createLessonDto) {
        Lesson lesson = lessonMapper.toLesson(createLessonDto);

        log.info("Create lesson: {}", lesson);
        Course course = courseRepository.findById(createLessonDto.courseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course with id = " + createLessonDto.courseId() + " not found"));

        lesson.setCourse(course);
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    @Transactional
    public Lesson update(UUID id, CreateLessonDto updateLessonDto) {

        if (lessonRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Lesson with id = " + id + " not found");
        }

        Lesson lesson = lessonMapper.toLesson(updateLessonDto);

        log.info("Update lesson: {}", lesson);
        Course course = courseRepository.findById(updateLessonDto.courseId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Course with id = " + updateLessonDto.courseId() + " not found"));

        lesson.setId(id);
        lesson.setCourse(course);
        lessonRepository.save(lesson);
        return lesson;
    }

    @Override
    public void delete(UUID id) {
        getById(id);
        log.info("Delete Lesson by id: {}", id);
        lessonRepository.deleteById(id);
    }
}
