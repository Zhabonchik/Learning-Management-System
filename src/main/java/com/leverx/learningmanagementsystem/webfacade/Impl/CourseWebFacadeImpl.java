package com.leverx.learningmanagementsystem.webfacade.Impl;

import com.leverx.learningmanagementsystem.dto.course.CourseResponseDto;
import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.mapper.course.CourseMapper;
import com.leverx.learningmanagementsystem.service.CourseService;
import com.leverx.learningmanagementsystem.service.CourseSettingsService;
import com.leverx.learningmanagementsystem.service.LessonService;
import com.leverx.learningmanagementsystem.service.StudentService;
import com.leverx.learningmanagementsystem.webfacade.CourseWebFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        Course course = courseService.getById(id);
        return courseMapper.toDto(course);
    }

    @Override
    @Transactional
    public CourseResponseDto create(CreateCourseDto createCourseDto) {
        Course course = courseMapper.toModel(createCourseDto);

        constructCourse(course, createCourseDto);

        Course createdCourse = courseService.create(course);
        return courseMapper.toDto(createdCourse);
    }

    @Override
    @Transactional
    public CourseResponseDto updateById(UUID id, CreateCourseDto createCourseDto) {
        Course course = courseMapper.toModel(createCourseDto);

        constructCourse(course, createCourseDto);
        course.setId(id);

        Course createdCourse = courseService.create(course);
        return courseMapper.toDto(createdCourse);
    }

    @Override
    public void deleteById(UUID id) {
        courseService.deleteById(id);
    }

    private void constructCourse(Course course, CreateCourseDto createCourseDto) {
        CourseSettings courseSettings = (createCourseDto.courseSettingsId() == null) ? null
                : courseSettingsService.getById(course.getId());
        List<Student> students = (createCourseDto.studentIds() == null) ? new ArrayList<>()
                : studentService.getAllByIdIn(createCourseDto.studentIds());
        List<Lesson> lessons = (createCourseDto.lessonIds() == null) ? new ArrayList<>()
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
