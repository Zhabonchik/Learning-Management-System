package com.leverx.learningmanagementsystem.service.impl;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.dto.course.GetCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.CourseSettings;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.EntityNotFoundException;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.IncorrectResultSizeException;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public Course getEntityById(UUID id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id = " + id + " not found"));
    }

    @Override
    public GetCourseDto getById(UUID id) {
        log.info("Get course with id {}", id);
        return courseMapper.toGetCourseDto(getEntityById(id));
    }

    @Override
    public List<GetCourseDto> getAllCourses() {
        log.info("Get all courses");
        return courseMapper.toGetCourseDtoList(courseRepository.findAll());
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

    private void saveCourse(Course course, CreateCourseDto createCourseDto) {
        Set<Lesson> lessons;
        Set<Student> students;

        if (createCourseDto.lessonIds() != null && CollectionUtils.isNotEmpty(createCourseDto.lessonIds())) {
            log.info("Fetching course's lessons");
            lessons = new HashSet<>(lessonRepository.findAllById(createCourseDto.lessonIds()));
            if (lessons.size() != createCourseDto.lessonIds().size()) {
                throw new IncorrectResultSizeException(
                        "Some of requested lessons don't exist");
            }
        } else {
            lessons = Collections.emptySet();
        }
        if (createCourseDto.studentIds() != null && CollectionUtils.isNotEmpty(createCourseDto.studentIds())) {
            log.info("Fetching course's students");
            students = StreamSupport.stream(studentRepository
                            .findAllById(createCourseDto.studentIds())
                            .spliterator(), false)
                    .collect(Collectors.toSet());
            if (students.size() != createCourseDto.studentIds().size()) {
                throw new IncorrectResultSizeException(
                        "Some of requested students don't exist");
            }
        } else {
            students = Collections.emptySet();
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
