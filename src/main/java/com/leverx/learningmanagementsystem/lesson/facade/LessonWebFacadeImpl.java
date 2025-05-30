package com.leverx.learningmanagementsystem.lesson.facade;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.dto.ClassroomLesson.CreateClassroomLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.CreateLessonDto;
import com.leverx.learningmanagementsystem.lesson.dto.LessonResponseDto;
import com.leverx.learningmanagementsystem.lesson.dto.VideoLesson.CreateVideoLessonDto;
import com.leverx.learningmanagementsystem.lesson.model.ClassroomLesson;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.lesson.mapper.LessonMapper;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.lesson.model.VideoLesson;
import com.leverx.learningmanagementsystem.lesson.service.LessonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return lessonMapper.toDtos(lessons);
    }

    @Override
    public Page<LessonResponseDto> getAll(Pageable pageable) {
        Page<Lesson> lessons = lessonService.getAll(pageable);
        return lessons.map(lessonMapper::toDto);
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

        updateCourse(lesson, dto);

        var createdLesson = lessonService.create(lesson);
        return lessonMapper.toDto(createdLesson);
    }

    @Override
    @Transactional
    public LessonResponseDto updateById(UUID id, CreateLessonDto dto) {
        var lesson = lessonService.getById(id);

        updateLesson(lesson, dto);
        updateCourse(lesson, dto);

        var updatedLesson = lessonService.updateById(lesson);
        return lessonMapper.toDto(updatedLesson);
    }

    @Override
    public void deleteById(UUID id) {
        lessonService.deleteById(id);
    }

    private void updateLesson(Lesson lesson, CreateLessonDto dto) {
        lesson.setTitle(dto.title());
        lesson.setDurationInMinutes(dto.durationInMinutes());
        if (lesson instanceof VideoLesson videoLesson && dto instanceof CreateVideoLessonDto videoLessonDto) {
            videoLesson.setPlatform(videoLessonDto.platform());
            videoLesson.setUrl(videoLessonDto.url());
        } else if (lesson instanceof ClassroomLesson classroomLesson && dto instanceof CreateClassroomLessonDto createClassroomLessonDto) {
            classroomLesson.setLocation(createClassroomLessonDto.location());
            classroomLesson.setCapacity(createClassroomLessonDto.capacity());
        }
    }

    private void updateCourse(Lesson lesson, CreateLessonDto dto) {
        var course = (isNull(dto.courseId())) ? null : courseService.getById(dto.courseId());
        replaceCourse(lesson, course);
    }

    private void replaceCourse(Lesson lesson, Course course) {
        if (!isNull(lesson.getCourse())) {
            lesson.getCourse().getLessons().remove(lesson);
        }

        lesson.setCourse(course);

        if (!isNull(course)) {
            course.getLessons().add(lesson);
        }
    }
}
