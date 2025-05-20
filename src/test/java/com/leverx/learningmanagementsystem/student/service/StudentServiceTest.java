package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.testutils.CourseTestUtils;
import com.leverx.learningmanagementsystem.testutils.StudentTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static com.leverx.learningmanagementsystem.testutils.CourseTestUtils.NON_EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NEW_STUDENT_COINS;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NEW_STUDENT_DATE_OF_BIRTH;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NEW_STUDENT_EMAIL;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NEW_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NEW_STUDENT_LAST_NAME;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.TOTAL_NUMBER_OF_STUDENTS;
import static com.leverx.learningmanagementsystem.testutils.TestUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.testutils.TestUtils.INSERT_SQL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnStudent() {
        Student student = studentService.getById(EXISTING_STUDENT_ID);

        assertNotNull(student);
        assertEquals(EXISTING_STUDENT_ID, student.getId());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllStudents() {
        List<Student> students = studentService.getAll();

        assertEquals(TOTAL_NUMBER_OF_STUDENTS, students.size());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenStudent_shouldReturnCreatedStudent() {
        Student newStudent = StudentTestUtils.initializeStudent();
        newStudent.setCourses(new ArrayList<>());

        Student createdStudent = studentService.create(newStudent);

        assertAll(
                () -> assertNotNull(createdStudent.getId()),
                () -> assertEquals(NEW_STUDENT_FIRST_NAME, createdStudent.getFirstName()),
                () -> assertEquals(NEW_STUDENT_LAST_NAME, createdStudent.getLastName()),
                () -> assertEquals(NEW_STUDENT_EMAIL, createdStudent.getEmail()),
                () -> assertEquals(NEW_STUDENT_DATE_OF_BIRTH, createdStudent.getDateOfBirth()),
                () -> assertEquals(NEW_STUDENT_COINS, createdStudent.getCoins())
        );
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_givenStudent_shouldReturnUpdatedStudent() {
        Student newStudent = StudentTestUtils.initializeStudent();
        newStudent.setId(EXISTING_STUDENT_ID);
        newStudent.setCourses(new ArrayList<>());

        Student updatedStudent = studentService.update(newStudent);
        int studentsAmount = studentService.getAll().size();

        assertAll(
                () -> assertEquals(TOTAL_NUMBER_OF_STUDENTS, studentsAmount),
                () -> assertEquals(EXISTING_STUDENT_ID, updatedStudent.getId()),
                () -> assertEquals(NEW_STUDENT_FIRST_NAME, updatedStudent.getFirstName()),
                () -> assertEquals(NEW_STUDENT_LAST_NAME, updatedStudent.getLastName()),
                () -> assertEquals(NEW_STUDENT_EMAIL, updatedStudent.getEmail()),
                () -> assertEquals(NEW_STUDENT_DATE_OF_BIRTH, updatedStudent.getDateOfBirth()),
                () -> assertEquals(NEW_STUDENT_COINS, updatedStudent.getCoins())
        );
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_givenStudent_shouldThrowExceptionAndRollbackTransaction() {
        Student newStudent = StudentTestUtils.initializeStudent();
        Course course = CourseTestUtils.initializeCourse();
        course.setId(NON_EXISTING_COURSE_ID);

        Exception ex = assertThrows(Exception.class,
                () -> {
                    newStudent.setId(EXISTING_STUDENT_ID);
                    newStudent.getCourses().add(course);
                    studentService.update(newStudent);
                });

        Student student = studentService.getById(EXISTING_STUDENT_ID);

        assertAll(
                () -> assertNotEquals(NEW_STUDENT_FIRST_NAME, student.getFirstName()),
                () -> assertFalse(student.getCourses().stream()
                        .map(Course::getId)
                        .toList()
                        .contains(NON_EXISTING_COURSE_ID))
        );
    }
}
