package com.leverx.learningmanagementsystem.course.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
import com.leverx.learningmanagementsystem.utils.exception.model.IncorrectResultSizeException;
import com.leverx.learningmanagementsystem.course.repository.CourseRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Override
    public Course getById(UUID id) {
        log.info("Get course [id = {}]", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found [id = {%s}]".formatted(id)));
    }

    @Override
    public Course getByIdForUpdate(UUID id) {
        log.info("Get course for update [id = {}]", id);
        return courseRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found [id = {%s}]".formatted(id)));
    }

    @Override
    @Transactional
    public List<Course> getAll() {
        log.info("Get all courses");
        List<Course> courses = courseRepository.findAllWithLessons();
        courses = !courses.isEmpty() ? courseRepository.findAllWithStudents() : courses;
        courses = !courses.isEmpty() ? courseRepository.findAllWithSettings() : courses;

        return courses;
    }

    @Override
    @Transactional
    public Page<Course> getAll(Pageable pageable) {
        log.info("Get all courses on page {}, with page size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Course> courses = courseRepository.findAll(pageable);
        List<Course> content = courses.getContent();

        content = !content.isEmpty() ? courseRepository.findAllWithLessons(content) : content;
        content = !content.isEmpty() ? courseRepository.findAllWithSettings(content) : content;
        content = !content.isEmpty() ? courseRepository.findAllWithStudents(content) : content;

        return new PageImpl<>(content, pageable, courses.getTotalElements());
    }

    @Override
    public List<Course> getAllByIdIn(List<UUID> ids) {
        List<Course> courses =  courseRepository.findAllByIdIn(ids);
        if (ids.size() != courses.size()) {
            throw new IncorrectResultSizeException("Some of requested courses don't exist");
        }
        return courses;
    }

    @Override
    public Course create(Course course) {
        log.info("Create course: {}", course);
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course update(Course course) {
        if (courseRepository.findById(course.getId()).isEmpty()) {
            throw new EntityNotFoundException("Course not found [id = {%s}]".formatted(course.getId()));
        }

        log.info("Update course [id = {}]", course.getId());
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllStartingBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return courseRepository.findAllBySettingsStartDateBetween(startDate, endDate);
    }

    @Override
    public void deleteById(UUID id) {
        getById(id);
        log.info("Delete course [id = {}]", id);
        courseRepository.deleteById(id);
    }
}
