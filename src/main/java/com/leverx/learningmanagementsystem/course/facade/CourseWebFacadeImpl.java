package com.leverx.learningmanagementsystem.course.facade;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.common.model.Lesson;
import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.course.mapper.CourseMapper;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.coursesettings.service.CourseSettingsService;
import com.leverx.learningmanagementsystem.lesson.common.service.LessonService;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@AllArgsConstructor
public class CourseWebFacadeImpl implements CourseWebFacade {

    private final CourseService courseService;
    private final CourseSettingsService courseSettingsService;
    private final LessonService lessonService;
    private final StudentService studentService;
    private final CourseMapper courseMapper;

    @Override
    public List<CourseResponseDto> getAll() {
        List<Course> courses = courseService.getAll();
        return courseMapper.toDtos(courses);
    }

    @Override
    public Page<CourseResponseDto> getAll(Pageable pageable) {
        Page<Course> courses = courseService.getAll(pageable);
        return courses.map(courseMapper::toDto);
    }

    @Override
    public CourseResponseDto getById(UUID id) {
        var course = courseService.getById(id);
        return courseMapper.toDto(course);
    }

    @Override
    @Transactional
    public CourseResponseDto create(CreateCourseDto createCourseDto) {
        var course = courseMapper.toModel(createCourseDto);

        updateRelations(course, createCourseDto);

        var createdCourse = courseService.create(course);
        return courseMapper.toDto(createdCourse);
    }

    @Override
    @Transactional
    public CourseResponseDto updateById(UUID id, CreateCourseDto createCourseDto) {
        var course = courseService.getById(id);

        update(course, createCourseDto);
        updateRelations(course, createCourseDto);

        var createdCourse = courseService.update(course);
        return courseMapper.toDto(createdCourse);
    }

    @Override
    public void deleteById(UUID id) {
        courseService.deleteById(id);
    }

    private void update(Course course, CreateCourseDto createCourseDto) {
        course.setTitle(createCourseDto.title());
        course.setDescription(createCourseDto.description());
        course.setPrice(createCourseDto.price());
        course.setCoinsPaid(createCourseDto.coinsPaid());
    }

    private void updateRelations(Course course, CreateCourseDto createCourseDto) {
        var courseSettings = (isNull(createCourseDto.courseSettingsId())) ? null
                : courseSettingsService.getById(createCourseDto.courseSettingsId());
        List<Student> students = (isNull(createCourseDto.studentIds())) ? new ArrayList<>()
                : studentService.getAllByIdIn(createCourseDto.studentIds());
        List<Lesson> lessons = (isNull(createCourseDto.lessonIds())) ? new ArrayList<>()
                : lessonService.getAllByIdIn(createCourseDto.lessonIds());

        course.setSettings(courseSettings);
        replaceStudents(course, students);
        replaceLessons(course, lessons);
    }

    private void replaceStudents(Course course, List<Student> students) {
        if (nonNull(course.getStudents())) {
            course.getStudents().forEach(student -> student.getCourses().remove(course));
            course.getStudents().clear();
        } else {
            course.setStudents(new ArrayList<>());
        }

        students.forEach(student -> {
            student.getCourses().add(course);
            course.getStudents().add(student);
        });
    }

    private void replaceLessons(Course course, List<Lesson> lessons) {
        if (nonNull(course.getLessons())) {
            course.getLessons().forEach(lesson -> lesson.setCourse(null));
            course.getLessons().clear();
        } else {
            course.setLessons(new ArrayList<>());
        }

        lessons.forEach(lesson -> {
            lesson.setCourse(course);
            course.getLessons().add(lesson);
        });
    }
}
