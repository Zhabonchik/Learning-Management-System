package com.leverx.learningmanagementsystem.course.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    private Course createCourse;

    @BeforeEach
    void init() {
        createCourse = Course.builder()
                .title("Test course")
                .description("This is a test course")
                .price(BigDecimal.valueOf(143))
                .coinsPaid(BigDecimal.valueOf(1430))
                .settings(null)
                .lessons(new ArrayList<>())
                .students(new ArrayList<>())
                .build();
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllCourses() {
        List<Course> courses = courseService.getAll();
        assertEquals(2, courses.size());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnCourse() {
        Course course = courseService.getById(UUID.fromString("b902261b-d1b9-4c58-869f-b04a5bbff4c9"));
        assertAll(
                () -> assertNotNull(course),
                () -> assertEquals(UUID.fromString("b902261b-d1b9-4c58-869f-b04a5bbff4c9"), course.getId()),
                () -> assertEquals("Test course 1", course.getTitle())
        );
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCourse_shouldReturnCreatedCourse() {
        Course course = courseService.create(createCourse);
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
    void update_givenCourse_shouldReturnUpdatedCourse() {
        createCourse.setId(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));
        Course updatedCourse = courseService.update(createCourse);

        int coursesCount = courseService.getAll().size();
        assertAll(
                () -> assertEquals(2, coursesCount),
                () -> assertEquals(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"), updatedCourse.getId()),
                () -> assertEquals(createCourse.getTitle(), updatedCourse.getTitle()),
                () -> assertEquals(createCourse.getDescription(), updatedCourse.getDescription()),
                () -> assertEquals(createCourse.getPrice(), updatedCourse.getPrice()),
                () -> assertEquals(createCourse.getCoinsPaid(), updatedCourse.getCoinsPaid())
        );
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_givenCourse_shouldThrowExceptionAndRollbackTransaction() {
        Lesson lesson = Lesson.builder()
                .id(UUID.fromString("ad019adb-c069-44c0-9624-f4d6b2de620b"))
                .title("Test Lesson")
                .durationInMinutes(80)
                .course(null)
                .build();

        Exception ex = assertThrows(Exception.class,
                () -> {
                    createCourse.setId(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));
                    createCourse.getLessons().add(lesson);
                    courseService.update(createCourse);
                }
        );

        Course course = courseService.getById(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));
        assertAll(
                () -> assertNotEquals(createCourse.getTitle(), course.getTitle()),
                () -> assertFalse(course.getLessons().stream()
                        .map(Lesson::getId).toList()
                        .contains(UUID.fromString("ad019adb-c069-44c0-9624-f4d6b2de620b")))
        );
    }

}
