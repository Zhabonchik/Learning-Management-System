package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.mapper.course.CourseMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.LessonRepository;
import com.leverx.learningmanagementsystem.repository.StudentRepository;
import com.leverx.learningmanagementsystem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository,
                             StudentRepository studentRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public GetCourseDto getById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return courseMapper.toGetCourseDto(course);
    }

    @Override
    public List<GetCourseDto> getAllCourses() {
        List<Course> courses = (List<Course>) courseRepository.findAll();
        return courses.stream()
                .map(courseMapper::toGetCourseDto)
                .toList();
    }

    @Override
    public GetCourseDto create(CreateCourseDto createCourseDto) {
        Course course = courseMapper.toCourse(createCourseDto);
        Set<Lesson> lessons = (Set<Lesson>) lessonRepository.findAllById(createCourseDto.lessonId());
        Set<Student> students = (Set<Student>) studentRepository.findAllById(createCourseDto.studentId());

        course.setLessons(lessons);
        course.setStudents(students);
        courseRepository.save(course);

        return courseMapper.toGetCourseDto(course);
    }

    @Override
    public GetCourseDto update(UUID id, CreateCourseDto updateCourseDto) {

        if (courseRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Course not found");
        }

        Course course = courseMapper.toCourse(updateCourseDto);
        course.setId(id);
        Set<Lesson> lessons = (Set<Lesson>) lessonRepository.findAllById(updateCourseDto.lessonId());
        Set<Student> students = (Set<Student>) studentRepository.findAllById(updateCourseDto.studentId());

        course.setLessons(lessons);
        course.setStudents(students);
        courseRepository.save(course);

        return courseMapper.toGetCourseDto(course);
    }

    @Override
    public void delete(UUID id) {
        courseRepository.deleteById(id);
    }
}
