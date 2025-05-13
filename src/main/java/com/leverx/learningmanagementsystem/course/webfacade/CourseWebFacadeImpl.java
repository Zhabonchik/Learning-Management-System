package com.leverx.learningmanagementsystem.course.webfacade;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.coursesettings.model.CourseSettings;
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
