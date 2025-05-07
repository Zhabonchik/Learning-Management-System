package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;
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
class StudentServiceTest {

    @Autowired
    StudentService studentService;

    Student createStudent;

    @BeforeEach
    void init() {
        createStudent = Student.builder()
                .firstName("A")
                .lastName("B")
                .email("email@gmail.com")
                .dateOfBirth(LocalDate.of(2005, 7, 23))
                .coins(new BigDecimal(1548))
                .courses(new ArrayList<>())
                .build();
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnStudent() {
        Student student = studentService.getById(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"));
        assertNotNull(student);
        assertEquals(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"), student.getId());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllStudents() {
        List<Student> students = studentService.getAll();
        assertEquals(2, students.size());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenStudent_shouldReturnCreatedStudent() {
        Student createdStudent = studentService.create(createStudent);
        assertAll(
                () -> assertNotNull(createdStudent.getId()),
                () -> assertEquals(createStudent.getFirstName(), createdStudent.getFirstName()),
                () -> assertEquals(createStudent.getLastName(), createdStudent.getLastName()),
                () -> assertEquals(createStudent.getEmail(), createdStudent.getEmail()),
                () -> assertEquals(createStudent.getDateOfBirth(), createdStudent.getDateOfBirth()),
                () -> assertEquals(createStudent.getCoins(), createdStudent.getCoins())
        );
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_givenStudent_shouldReturnUpdatedStudent() {
        createStudent.setId(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"));
        Student updatedStudent = studentService.update(createStudent);
        int studentsAmount = studentService.getAll().size();
        assertAll(
                () -> assertEquals(2, studentsAmount),
                () -> assertEquals(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"), updatedStudent.getId()),
                () -> assertEquals(createStudent.getFirstName(), updatedStudent.getFirstName()),
                () -> assertEquals(createStudent.getLastName(), updatedStudent.getLastName()),
                () -> assertEquals(createStudent.getEmail(), updatedStudent.getEmail()),
                () -> assertEquals(createStudent.getDateOfBirth(), updatedStudent.getDateOfBirth()),
                () -> assertEquals(createStudent.getCoins(), updatedStudent.getCoins())
        );
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void update_givenStudent_shouldThrowExceptionAndRollbackTransaction() {

        Course course = Course.builder()
                .id(UUID.fromString("ddf747d2-f5de-4a2e-b8b9-66d8bd7e357e"))
                .title("Test course")
                .description("This is a test course")
                .price(BigDecimal.valueOf(143))
                .coinsPaid(BigDecimal.valueOf(1430))
                .settings(null)
                .lessons(new ArrayList<>())
                .students(new ArrayList<>())
                .build();

        Exception ex = assertThrows(Exception.class,
                () -> {
                    createStudent.setId(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"));
                    createStudent.getCourses().add(course);
                    studentService.update(createStudent);
                });

        Student student = studentService.getById(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"));
        assertAll(
                () -> assertNotEquals(createStudent.getFirstName(), student.getFirstName()),
                () -> assertFalse(student.getCourses().stream()
                        .map(Course::getId)
                        .toList()
                        .contains(UUID.fromString("ddf747d2-f5de-4a2e-b8b9-66d8bd7e357e")))
        );
    }
}
