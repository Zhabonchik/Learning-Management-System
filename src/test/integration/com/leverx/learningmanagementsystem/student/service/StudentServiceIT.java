package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.student.model.Student;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import com.leverx.learningmanagementsystem.utils.CourseITUtils;
import com.leverx.learningmanagementsystem.utils.StudentITUtils;
import java.util.ArrayList;
import java.util.List;

import static com.leverx.learningmanagementsystem.utils.CourseITUtils.NON_EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NEW_STUDENT_COINS;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NEW_STUDENT_DATE_OF_BIRTH;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NEW_STUDENT_EMAIL;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NEW_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NEW_STUDENT_LAST_NAME;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.TOTAL_NUMBER_OF_STUDENTS;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.initializeStudent;
import static com.leverx.learningmanagementsystem.utils.ITUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.utils.ITUtils.INSERT_SQL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Tag("integration")
class StudentServiceIT {

    @Autowired
    StudentService studentService;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnStudent() {
        // when
        Student student = studentService.getById(EXISTING_STUDENT_ID);

        // then
        assertNotNull(student);
        assertEquals(EXISTING_STUDENT_ID, student.getId());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllStudents() {
        // when
        List<Student> students = studentService.getAll();

        // then
        assertEquals(TOTAL_NUMBER_OF_STUDENTS, students.size());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenStudent_shouldReturnCreatedStudent() {
        // given
        Student newStudent = initializeStudent();
        newStudent.setCourses(new ArrayList<>());

        // when
        Student createdStudent = studentService.create(newStudent);

        // then
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
    @WithMockUser(roles = "USER")
    void update_givenStudent_shouldReturnUpdatedStudent() {
        // given
        Student updateStudent = studentService.getById(EXISTING_STUDENT_ID);
        updateStudent.setFirstName(NEW_STUDENT_FIRST_NAME);
        updateStudent.setLastName(NEW_STUDENT_LAST_NAME);
        updateStudent.setEmail(NEW_STUDENT_EMAIL);
        updateStudent.setDateOfBirth(NEW_STUDENT_DATE_OF_BIRTH);
        updateStudent.setCoins(NEW_STUDENT_COINS);

        // when
        Student updatedStudent = studentService.update(updateStudent);
        int studentsAmount = studentService.getAll().size();

        // then
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
        // given
        Student newStudent = StudentITUtils.initializeStudent();
        Course course = CourseITUtils.initializeCourse();
        course.setId(NON_EXISTING_COURSE_ID);

        // when
        Exception ex = assertThrows(Exception.class,
                () -> {
                    newStudent.setId(EXISTING_STUDENT_ID);
                    newStudent.getCourses().add(course);
                    studentService.update(newStudent);
                });

        Student student = studentService.getById(EXISTING_STUDENT_ID);

        // then
        assertAll(
                () -> assertNotEquals(NEW_STUDENT_FIRST_NAME, student.getFirstName()),
                () -> assertFalse(student.getCourses().stream()
                        .map(Course::getId)
                        .toList()
                        .contains(NON_EXISTING_COURSE_ID))
        );
    }
}
