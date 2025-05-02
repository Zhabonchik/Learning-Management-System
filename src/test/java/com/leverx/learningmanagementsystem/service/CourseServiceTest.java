package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.exception.EntityValidationException.IncorrectResultSizeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    private CreateCourseDto createCourseDto;

    @BeforeEach
    public void init() {
        createCourseDto = new CreateCourseDto("Test course", "This is a test course",
                BigDecimal.valueOf(143), BigDecimal.valueOf(1430), null, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testGetAll() {
        List<Course> courses = courseService.getAll();
        assertEquals(6, courses.size());
    }

    @Test
    public void testGetById() {
        Course course = courseService.getById(UUID.fromString("f74405a1-25e9-4b19-b366-bf4be157d138"));
        assertAll(
                () -> assertNotNull(course),
                () -> assertEquals(UUID.fromString("f74405a1-25e9-4b19-b366-bf4be157d138"), course.getId()),
                () -> assertEquals("Test course 5", course.getTitle())
        );
    }

    @Test
    public void testCreate() {
        Course course = courseService.create(createCourseDto);
        assertAll(
                () -> assertNotNull(course),
                () -> assertEquals("Test course", course.getTitle()),
                () -> assertEquals("This is a test course", course.getDescription()),
                () -> assertEquals(BigDecimal.valueOf(143), course.getPrice()),
                () -> assertEquals(BigDecimal.valueOf(1430), course.getCoinsPaid())
        );
    }

    @Test
    public void testUpdate() {
        Course updatedCourse = courseService.update(
                UUID.fromString("f74405a1-25e9-4b19-b366-bf4be157d138"),
                createCourseDto
        );
        int coursesCount = courseService.getAll().size();
        assertAll(
                () -> assertEquals(6, coursesCount),
                () -> assertEquals(UUID.fromString("f74405a1-25e9-4b19-b366-bf4be157d138"), updatedCourse.getId()),
                () -> assertEquals(createCourseDto.title(), updatedCourse.getTitle()),
                () -> assertEquals(createCourseDto.description(), updatedCourse.getDescription()),
                () -> assertEquals(createCourseDto.price(), updatedCourse.getPrice()),
                () -> assertEquals(createCourseDto.coinsPaid(), updatedCourse.getCoinsPaid())
        );
    }

    @Test
    public void testUpdateThrowIncorrectResultSizeExceptionAndRollbackTransaction() {
        Exception ex = assertThrows(IncorrectResultSizeException.class,
                () -> {
                    createCourseDto.lessonIds().add(UUID.fromString("ad019adb-c069-44c0-9624-f4d6b2de620b"));
                    courseService.update(UUID.fromString("f74405a1-25e9-4b19-b366-bf4be157d138"), createCourseDto);
                }
        );
        assertEquals("Some of requested lessons don't exist", ex.getMessage());
        Course course = courseService.getById(UUID.fromString("f74405a1-25e9-4b19-b366-bf4be157d138"));
        assertAll(
                () -> assertNotEquals(createCourseDto.title(), course.getTitle()),
                () -> assertFalse(course.getLessons().stream()
                        .map(Lesson::getId).toList()
                        .contains(UUID.fromString("ad019adb-c069-44c0-9624-f4d6b2de620b")))
        );
    }

}
