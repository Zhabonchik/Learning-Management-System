package com.leverx.learningmanagementsystem.integration.course.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.lesson.model.Lesson;
import org.junit.jupiter.api.Tag;
import testutils.CourseTestUtils;
import testutils.LessonTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static testutils.CourseTestUtils.EXISTING_COURSE_ID;
import static testutils.CourseTestUtils.EXISTING_COURSE_TITLE;
import static testutils.CourseTestUtils.NEW_COURSE_COINS_PAID;
import static testutils.CourseTestUtils.NEW_COURSE_DESCRIPTION;
import static testutils.CourseTestUtils.NEW_COURSE_PRICE;
import static testutils.CourseTestUtils.NEW_COURSE_TITLE;
import static testutils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static testutils.LessonTestUtils.NON_EXISTING_LESSON_ID;
import static testutils.TestUtils.CLEAN_SQL;
import static testutils.TestUtils.INSERT_SQL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Tag("integration-test")
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllCourses() {
        // when
        List<Course> courses = courseService.getAll();

        // then
        assertEquals(TOTAL_NUMBER_OF_COURSES, courses.size());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnCourse() {
        // when
        Course course = courseService.getById(EXISTING_COURSE_ID);

        // then
        assertAll(
                () -> assertNotNull(course),
                () -> assertEquals(EXISTING_COURSE_ID, course.getId()),
                () -> assertEquals(EXISTING_COURSE_TITLE, course.getTitle())
        );
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenCourse_shouldReturnCreatedCourse() {
        // given
        Course newCourse = CourseTestUtils.initializeCourse();
        newCourse.setLessons(new ArrayList<>());

        // when
        Course course = courseService.create(newCourse);

        // then
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
    @WithMockUser(roles = "USER")
    void update_givenCourse_shouldReturnUpdatedCourse() {
        // given
        Course courseToUpdate = courseService.getById(EXISTING_COURSE_ID);
        courseToUpdate.setTitle(NEW_COURSE_TITLE);
        courseToUpdate.setDescription(NEW_COURSE_DESCRIPTION);
        courseToUpdate.setPrice(NEW_COURSE_PRICE);
        courseToUpdate.setCoinsPaid(NEW_COURSE_COINS_PAID);

        // when
        Course updatedCourse = courseService.update(courseToUpdate);
        int coursesCount = courseService.getAll().size();

        // then
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
        // given
        Course newCourse = CourseTestUtils.initializeCourse();
        Lesson nonExistingLesson = LessonTestUtils.initializeLesson();
        nonExistingLesson.setId(NON_EXISTING_LESSON_ID);

        // when
        Exception ex = assertThrows(Exception.class,
                () -> {
                    newCourse.setId(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));
                    newCourse.getLessons().add(nonExistingLesson);
                    courseService.update(newCourse);
                }
        );

        Course course = courseService.getById(UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));

        // then
        assertAll(
                () -> assertNotEquals(newCourse.getTitle(), course.getTitle()),
                () -> assertFalse(course.getLessons().stream()
                        .map(Lesson::getId).toList()
                        .contains(NON_EXISTING_LESSON_ID))
        );
    }

}
