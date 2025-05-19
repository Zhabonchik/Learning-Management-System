package com.leverx.learningmanagementsystem.course.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import com.leverx.learningmanagementsystem.testutils.CourseTestUtils;
import com.leverx.learningmanagementsystem.testutils.LessonTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.EXISTING_COURSE_TITLE;
import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.NEW_COURSE_COINS_PAID;
import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.NEW_COURSE_DESCRIPTION;
import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.NEW_COURSE_PRICE;
import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.NEW_COURSE_TITLE;
import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static com.leverx.learningmanagementsystem.testutils.LessonTestUtils.NON_EXISTING_LESSON_ID;
import static com.leverx.learningmanagementsystem.testutils.TestUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.testutils.TestUtils.INSERT_SQL;
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

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllCourses() {
        List<Course> courses = courseService.getAll();

        assertEquals(TOTAL_NUMBER_OF_COURSES, courses.size());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnCourse() {
        Course course = courseService.getById(EXISTING_COURSE_ID);

        assertAll(
                () -> assertNotNull(course),
                () -> assertEquals(EXISTING_COURSE_ID, course.getId()),
                () -> assertEquals(EXISTING_COURSE_TITLE, course.getTitle())
        );
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCourse_shouldReturnCreatedCourse() {
        Course newCourse = CourseTestUtils.initializeCourse();
        newCourse.setLessons(new ArrayList<>());

        Course course = courseService.create(newCourse);

        assertAll(
                () -> assertNotNull(course),
                () -> assertEquals(NEW_COURSE_TITLE, course.getTitle()),
                () -> assertEquals(NEW_COURSE_DESCRIPTION, course.getDescription()),
                () -> assertEquals(NEW_COURSE_PRICE, course.getPrice()),
                () -> assertEquals(NEW_COURSE_COINS_PAID, course.getCoinsPaid())
        );
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_givenCourse_shouldReturnUpdatedCourse() {
        Course newCourse = CourseTestUtils.initializeCourse();
        newCourse.setLessons(new ArrayList<>());
        newCourse.setId(EXISTING_COURSE_ID);

        Course updatedCourse = courseService.update(newCourse);
        int coursesCount = courseService.getAll().size();

        assertAll(
                () -> assertEquals(TOTAL_NUMBER_OF_COURSES, coursesCount),
                () -> assertEquals(EXISTING_COURSE_ID, updatedCourse.getId()),
                () -> assertEquals(NEW_COURSE_TITLE, updatedCourse.getTitle()),
                () -> assertEquals(NEW_COURSE_DESCRIPTION, updatedCourse.getDescription()),
                () -> assertEquals(NEW_COURSE_PRICE, updatedCourse.getPrice()),
                () -> assertEquals(NEW_COURSE_COINS_PAID, updatedCourse.getCoinsPaid())
        );
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_givenCourse_shouldThrowExceptionAndRollbackTransaction() {
        Course newCourse = CourseTestUtils.initializeCourse();
        Lesson nonExistingLesson = LessonTestUtils.initializeLesson();
        nonExistingLesson.setId(NON_EXISTING_LESSON_ID);

        Exception ex = assertThrows(Exception.class,
                () -> {
                    newCourse.setId(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));
                    newCourse.getLessons().add(nonExistingLesson);
                    courseService.update(newCourse);
                }
        );

        Course course = courseService.getById(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));

        assertAll(
                () -> assertNotEquals(newCourse.getTitle(), course.getTitle()),
                () -> assertFalse(course.getLessons().stream()
                        .map(Lesson::getId).toList()
                        .contains(NON_EXISTING_LESSON_ID))
        );
    }

}
