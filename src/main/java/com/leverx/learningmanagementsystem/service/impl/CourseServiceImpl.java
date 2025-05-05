package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.exception.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.mapper.course.CourseMapper;
import com.leverx.learningmanagementsystem.repository.CourseRepository;
import com.leverx.learningmanagementsystem.repository.CourseSettingsRepository;
import com.leverx.learningmanagementsystem.repository.LessonRepository;
import com.leverx.learningmanagementsystem.repository.StudentRepository;
import com.leverx.learningmanagementsystem.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final CourseSettingsRepository courseSettingsRepository;
    private final CourseMapper courseMapper;

    @Override
    public Course getById(UUID id) {
        log.info("Get course with id {}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id = " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getAll() {
        log.info("Get all courses");
        List<Course> courses = courseRepository.findAllWithLessons();
        courses = !courses.isEmpty() ? courseRepository.findAllWithStudents() : courses;
        courses = !courses.isEmpty() ? courseRepository.findAllWithSettings() : courses;

        return courses;
    }

    @Override
    @Transactional
    public Course create(CreateCourseDto createCourseDto) {
        Course course = courseMapper.toModel(createCourseDto);

        log.info("Create course with id {}", course.getId());
        save(course, createCourseDto);

        return course;
    }

    @Override
    @Transactional
    public Course update(UUID id, CreateCourseDto updateCourseDto) {

        if (courseRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Course with id = " + id + " not found");
        }

        Course course = courseMapper.toModel(updateCourseDto);
        course.setId(id);

        log.info("Update course with id {}", course.getId());
        save(course, updateCourseDto);

        return course;
    }

    @Override
    public List<Course> getAllStartingBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return courseRepository.findAllBySettingsStartDateBetween(startDate, endDate);
    }

    @Override
    public void delete(UUID id) {
        getById(id);
        log.info("Delete course with id {}", id);
        courseRepository.deleteById(id);
    }

    private void save(Course course, CreateCourseDto createCourseDto) {
        List<Lesson> lessons;
        List<Student> students;

        if (createCourseDto.lessonIds() != null && CollectionUtils.isNotEmpty(createCourseDto.lessonIds())) {
            log.info("Fetching course's lessons");
            lessons = lessonRepository.findAllByIdIn(createCourseDto.lessonIds());
            if (lessons.size() != createCourseDto.lessonIds().size()) {
                throw new IncorrectResultSizeException(
                        "Some of requested lessons don't exist");
            }
        } else {
            lessons = Collections.emptyList();
        }
        if (createCourseDto.studentIds() != null && CollectionUtils.isNotEmpty(createCourseDto.studentIds())) {
            log.info("Fetching course's students");
            students = studentRepository.findAllByIdIn(createCourseDto.studentIds());
            if (students.size() != createCourseDto.studentIds().size()) {
                throw new IncorrectResultSizeException(
                        "Some of requested students don't exist");
            }
        } else {
            students = Collections.emptyList();
        }

        UUID settingsId = createCourseDto.courseSettingsId();
        if (settingsId != null) {
            log.info("Fetching course settings with id = {}", settingsId);
            CourseSettings courseSettings = courseSettingsRepository.findById(settingsId)
                    .orElseThrow(() -> new EntityNotFoundException("Course settings with id = " + settingsId + " not found"));
            course.setSettings(courseSettings);
        } else {
            course.setSettings(null);
        }

        course.setLessons(lessons);
        course.setStudents(students);

        courseRepository.save(course);
    }
}
