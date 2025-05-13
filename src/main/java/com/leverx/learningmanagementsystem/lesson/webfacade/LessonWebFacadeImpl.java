package com.leverx.learningmanagementsystem.lesson.webfacade;

import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.lesson.mapper.LessonMapper;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.lesson.service.LessonService;
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
    @Transactional
    public LessonResponseDto updateById(UUID id, CreateLessonDto dto) {
        Course course = courseService.getById(dto.courseId());
        Lesson lesson = lessonMapper.toModel(dto);
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
