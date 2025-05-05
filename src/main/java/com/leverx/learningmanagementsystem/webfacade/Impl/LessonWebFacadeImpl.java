package com.leverx.learningmanagementsystem.webfacade.Impl;

import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import com.leverx.learningmanagementsystem.dto.lesson.LessonResponseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.mapper.lesson.LessonMapper;
import com.leverx.learningmanagementsystem.service.CourseService;
import com.leverx.learningmanagementsystem.service.LessonService;
import com.leverx.learningmanagementsystem.webfacade.LessonWebFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class LessonWebFacadeImpl implements LessonWebFacade {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;
    private final CourseService courseService;

    @Override
    public List<LessonResponseDto> getAll() {
        List<Lesson> lessons = lessonService.getAll();
        return lessonMapper.toDtos(lessons);
    }

    @Override
    public LessonResponseDto getById(UUID id) {
        Lesson lesson = lessonService.getById(id);
        return lessonMapper.toDto(lesson);
    }

    @Override
    @Transactional
    public LessonResponseDto create(CreateLessonDto dto) {
        Lesson lesson = lessonMapper.toModel(dto);

        Course course = courseService.getById(dto.courseId());
        lesson.setCourse(course);

        Lesson createdLesson = lessonService.create(lesson);
        return lessonMapper.toDto(createdLesson);
    }

    @Override
    public LessonResponseDto updateById(UUID id, CreateLessonDto dto) {
        Lesson lesson = lessonMapper.toModel(dto);
        Course course = courseService.getById(dto.courseId());
        lesson.setCourse(course);
        lesson.setId(id);
        Lesson updatedLesson = lessonService.updateById(lesson);
        return lessonMapper.toDto(updatedLesson);
    }

    @Override
    public void deleteById(UUID id) {
        lessonService.deleteById(id);
    }
}
