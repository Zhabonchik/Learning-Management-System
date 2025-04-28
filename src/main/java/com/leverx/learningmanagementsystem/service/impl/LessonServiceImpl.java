package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.GetLessonDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.mapper.lesson.LessonMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.LessonRepository;
import com.leverx.learningmanagementsystem.service.LessonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        return lessonMapper.toGetLessonDto(lesson);
    }

    @Override
    public List<GetLessonDto> getAllLessons() {
        List<Lesson> lessons = (List<Lesson>) lessonRepository.findAll();
        return lessons.stream()
                .map(lessonMapper::toGetLessonDto)
                .collect(Collectors.toList());
    }

    @Override
    public GetLessonDto create(CreateLessonDto createLessonDto) {
        Lesson lesson = lessonMapper.toLesson(createLessonDto);
        Course course = courseRepository.findById(createLessonDto.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        lesson.setCourse(course);
        lessonRepository.save(lesson);

        return lessonMapper.toGetLessonDto(lesson);
    }

    @Override
    public GetLessonDto update(UUID id, CreateLessonDto updateLessonDto) {

        if (lessonRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Lesson not found");
        }

        Lesson lesson = lessonMapper.toLesson(updateLessonDto);
        Course course = courseRepository.findById(updateLessonDto.courseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        lesson.setId(id);
        lesson.setCourse(course);
        lessonRepository.save(lesson);

        return lessonMapper.toGetLessonDto(lesson);
    }

    @Override
    public void delete(UUID id) {
        lessonRepository.deleteById(id);
    }
}
