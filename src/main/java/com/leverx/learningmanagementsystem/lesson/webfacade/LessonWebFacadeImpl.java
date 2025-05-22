package com.leverx.learningmanagementsystem.lesson.webfacade;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.lesson.mapper.LessonMapper;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.lesson.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class LessonWebFacadeImpl implements LessonWebFacade {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;
    private final CourseService courseService;

    @Override
    public List<LessonResponseDto> getAll() {
        List<Lesson> lessons = lessonService.getAll();
        List<LessonResponseDto> lessonDtos = lessonMapper.toDtos(lessons);
        return lessonDtos;
    }

    @Override
    public LessonResponseDto getById(UUID id) {
        var lesson = lessonService.getById(id);
        return lessonMapper.toDto(lesson);
    }

    @Override
    @Transactional
    public LessonResponseDto create(CreateLessonDto dto) {
        var lesson = lessonMapper.toModel(dto);

        constructLesson(lesson, dto);

        var createdLesson = lessonService.create(lesson);
        return lessonMapper.toDto(createdLesson);
    }

    @Override
    @Transactional
    public LessonResponseDto updateById(UUID id, CreateLessonDto dto) {
        var lesson = lessonMapper.toModel(dto);
        lesson.setId(id);

        constructLesson(lesson, dto);

        var updatedLesson = lessonService.updateById(lesson);
        return lessonMapper.toDto(updatedLesson);
    }

    @Override
    public void deleteById(UUID id) {
        lessonService.deleteById(id);
    }

    private void constructLesson(Lesson lesson, CreateLessonDto dto) {
        var course = (isNull(dto.courseId())) ? null : courseService.getById(dto.courseId());
        lesson.setCourse(course);
        if (!isNull(course)) {
            course.getLessons().add(lesson);
        }
    }
}
