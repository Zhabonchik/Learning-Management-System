package com.leverx.learningmanagementsystem.course.webfacade;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.course.mapper.CourseMapper;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.coursesettings.service.CourseSettingsService;
import com.leverx.learningmanagementsystem.lesson.service.LessonService;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

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
    public CourseResponseDto getById(UUID id) {
        var course = courseService.getById(id);
        return courseMapper.toDto(course);
    }

    @Override
    @Transactional
    public CourseResponseDto create(CreateCourseDto createCourseDto) {
        var course = courseMapper.toModel(createCourseDto);

        constructCourse(course, createCourseDto);

        var createdCourse = courseService.create(course);
        return courseMapper.toDto(createdCourse);
    }

    @Override
    @Transactional
    public CourseResponseDto updateById(UUID id, CreateCourseDto createCourseDto) {
        var course = courseMapper.toModel(createCourseDto);

        constructCourse(course, createCourseDto);
        course.setId(id);

        var createdCourse = courseService.create(course);
        return courseMapper.toDto(createdCourse);
    }

    @Override
    public void deleteById(UUID id) {
        courseService.deleteById(id);
    }

    private void constructCourse(Course course, CreateCourseDto createCourseDto) {
        var courseSettings = (isNull(createCourseDto.courseSettingsId())) ? null
                : courseSettingsService.getById(createCourseDto.courseSettingsId());
        List<Student> students = (isNull(createCourseDto.studentIds())) ? new ArrayList<>()
                : studentService.getAllByIdIn(createCourseDto.studentIds());
        List<Lesson> lessons = (isNull(createCourseDto.lessonIds())) ? new ArrayList<>()
                : lessonService.getAllByIdIn(createCourseDto.lessonIds());

        course.setSettings(courseSettings);
        course.setStudents(students);
        course.setLessons(lessons);

        students.forEach(student -> {
            if (!student.getCourses().contains(course)){
                student.getCourses().add(course);
            }
        });
        lessons.forEach(lesson -> lesson.setCourse(course));
    }
}
