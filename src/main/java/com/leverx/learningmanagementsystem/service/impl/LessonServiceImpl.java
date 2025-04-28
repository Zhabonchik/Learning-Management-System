package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.GetLessonDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.mapper.lesson.LessonMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.LessonRepository;
import com.leverx.learningmanagementsystem.service.LessonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, CourseRepository courseRepository,
                             LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.lessonMapper = lessonMapper;
    }

    @Override
    public GetLessonDto getById(UUID id) {
        log.info("Get Lesson by id: {}", id);
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson with id = " + id + " not found"));
        return lessonMapper.toGetLessonDto(lesson);
    }

    @Override
    public List<GetLessonDto> getAllLessons() {
        log.info("Get all Lessons");
        List<Lesson> lessons = (List<Lesson>) lessonRepository.findAll();
        return lessons.stream()
                .map(lessonMapper::toGetLessonDto)
                .collect(Collectors.toList());
    }

    @Override
    public GetLessonDto create(CreateLessonDto createLessonDto) {
        Lesson lesson = lessonMapper.toLesson(createLessonDto);

        log.info("Create lesson: {}", lesson);
        Course course = courseRepository.findById(createLessonDto.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Course with id = " + createLessonDto.courseId() + " not found"));

        lesson.setCourse(course);
        lessonRepository.save(lesson);

        return lessonMapper.toGetLessonDto(lesson);
    }

    @Override
    public GetLessonDto update(UUID id, CreateLessonDto updateLessonDto) {

        if (lessonRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Lesson with id = " + id + " not found");
        }

        Lesson lesson = lessonMapper.toLesson(updateLessonDto);

        log.info("Update lesson: {}", lesson);
        Course course = courseRepository.findById(updateLessonDto.courseId())
                .orElseThrow(() -> new EntityNotFoundException("Course with id = " + updateLessonDto.courseId() + " not found"));

        lesson.setId(id);
        lesson.setCourse(course);
        lessonRepository.save(lesson);

        return lessonMapper.toGetLessonDto(lesson);
    }

    @Override
    public void delete(UUID id) {
        log.info("Delete Lesson by id: {}", id);
        lessonRepository.deleteById(id);
    }
}
