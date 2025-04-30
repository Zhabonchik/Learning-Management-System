package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.MismatchException;
import com.leverx.learningmanagementsystem.mapper.course.CourseMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.repository.LessonRepository;
import com.leverx.learningmanagementsystem.repository.StudentRepository;
import com.leverx.learningmanagementsystem.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final CourseSettingsRepository courseSettingsRepository;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, LessonRepository lessonRepository,
                             StudentRepository studentRepository, CourseMapper courseMapper,
                             CourseSettingsRepository courseSettingsRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.courseSettingsRepository = courseSettingsRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public GetCourseDto getById(UUID id) {
        log.info("Get course with id {}", id);
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id = " + id + " not found"));
        return courseMapper.toGetCourseDto(course);
    }

    @Override
    public List<GetCourseDto> getAllCourses() {
        log.info("Get all courses");
        List<Course> courses = (List<Course>) courseRepository.findAll();
        return courses.stream()
                .map(courseMapper::toGetCourseDto)
                .toList();
    }

    @Override
    @Transactional
    public GetCourseDto create(CreateCourseDto createCourseDto) {
        Course course = courseMapper.toCourse(createCourseDto);

        log.info("Create course with id {}", course.getId());
        saveCourse(course, createCourseDto);

        return courseMapper.toGetCourseDto(course);
    }

    @Override
    @Transactional
    public GetCourseDto update(UUID id, CreateCourseDto updateCourseDto) {

        if (courseRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Course with id = " + id + " not found");
        }

        Course course = courseMapper.toCourse(updateCourseDto);
        course.setId(id);

        log.info("Update course with id {}", course.getId());
        saveCourse(course, updateCourseDto);

        return courseMapper.toGetCourseDto(course);
    }

    @Override
    public List<Course> getAllStartingBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return courseRepository.findAllStartingTheFollowingDay(startDate, endDate);
    }

    @Override
    public void delete(UUID id) {
        log.info("Delete course with id {}", id);
        courseRepository.deleteById(id);
    }

    public void saveCourse(Course course, CreateCourseDto createCourseDto) {
        Set<Lesson> lessons;
        Set<Student> students;

        if (createCourseDto.lessonId() != null && createCourseDto.lessonId().isEmpty()) {
            log.info("Fetching course's lessons");
            lessons = StreamSupport.stream(lessonRepository
                            .findAllById(createCourseDto.lessonId())
                            .spliterator(), false)
                    .collect(Collectors.toSet());
            if (lessons.size() != createCourseDto.lessonId().size()) {
                throw new MismatchException("Numbers of lessons in course with id = " + course.getId()
                        + " and requested lessons mismatch (" + lessons.size() + " != " + createCourseDto.lessonId().size() + ")");
            }
        } else {
            lessons = Collections.emptySet();
        }
        if (createCourseDto.studentId() != null && !createCourseDto.studentId().isEmpty()) {
            log.info("Fetching course's students");
            students = StreamSupport.stream(studentRepository
                            .findAllById(createCourseDto.studentId())
                            .spliterator(), false)
                    .collect(Collectors.toSet());
            if (students.size() != createCourseDto.studentId().size()) {
                throw new MismatchException("Numbers of students in course with id = " + course.getId()
                        + " and requested students mismatch (" + students.size() + " != " + createCourseDto.studentId().size() + ")");
            }
        } else {
            students = Collections.emptySet();
        }

        log.info("Fetching course settings with id = {}", createCourseDto.courseSettingsId());
        CourseSettings courseSettings = courseSettingsRepository.findById(createCourseDto.courseSettingsId())
                .orElseThrow(() -> new EntityNotFoundException("Course settings with id = "
                        + createCourseDto.courseSettingsId() + " not found"));

        course.setSettings(courseSettings);
        course.setLessons(lessons);
        course.setStudents(students);

        courseRepository.save(course);
    }
}
