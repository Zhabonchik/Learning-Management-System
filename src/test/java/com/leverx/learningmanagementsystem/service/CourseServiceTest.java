package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Lesson;
import com.leverx.learningmanagementsystem.exception.IncorrectResultSizeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    private CreateCourseDto createCourseDto;

    @BeforeEach
    void init() {
        createCourseDto = new CreateCourseDto("Test course", "This is a test course",
                BigDecimal.valueOf(143), BigDecimal.valueOf(1430), null, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetAll() {
        List<Course> courses = courseService.getAll();
        assertEquals(2, courses.size());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetById() {
        Course course = courseService.getById(UUID.fromString("b902261b-d1b9-4c58-869f-b04a5bbff4c9"));
        assertAll(
                () -> assertNotNull(course),
                () -> assertEquals(UUID.fromString("b902261b-d1b9-4c58-869f-b04a5bbff4c9"), course.getId()),
                () -> assertEquals("Test course 1", course.getTitle())
        );
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testCreate() {
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
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testUpdate() {
        Course updatedCourse = courseService.updateById(
                UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"),
                createCourseDto
        );
        int coursesCount = courseService.getAll().size();
        assertAll(
                () -> assertEquals(2, coursesCount),
                () -> assertEquals(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"), updatedCourse.getId()),
                () -> assertEquals(createCourseDto.title(), updatedCourse.getTitle()),
                () -> assertEquals(createCourseDto.description(), updatedCourse.getDescription()),
                () -> assertEquals(createCourseDto.price(), updatedCourse.getPrice()),
                () -> assertEquals(createCourseDto.coinsPaid(), updatedCourse.getCoinsPaid())
        );
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testUpdateThrowIncorrectResultSizeExceptionAndRollbackTransaction() {
        Exception ex = assertThrows(IncorrectResultSizeException.class,
                () -> {
                    createCourseDto.lessonIds().add(UUID.fromString("ad019adb-c069-44c0-9624-f4d6b2de620b"));
                    courseService.updateById(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"), createCourseDto);
                }
        );
        assertEquals("Some of requested lessons don't exist", ex.getMessage());
        Course course = courseService.getById(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));
        assertAll(
                () -> assertNotEquals(createCourseDto.title(), course.getTitle()),
                () -> assertFalse(course.getLessons().stream()
                        .map(Lesson::getId).toList()
                        .contains(UUID.fromString("ad019adb-c069-44c0-9624-f4d6b2de620b")))
        );
    }

}
