package com.leverx.learningmanagementsystem.service;

import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import com.leverx.learningmanagementsystem.entity.Course;
import com.leverx.learningmanagementsystem.entity.Student;
import com.leverx.learningmanagementsystem.exception.IncorrectResultSizeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StudentServiceTest {

    @Autowired
    StudentService studentService;

    CreateStudentDto createStudentDto;

    @BeforeEach
    public void init() {
        createStudentDto = new CreateStudentDto("A", "B", "email@gmail.com",
                LocalDate.of(2005, 7, 23), new BigDecimal(1548), new ArrayList<>());
    }

    @Test
    public void testGetStudentById() {
        Student student = studentService.getById(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"));
        assertNotNull (student);
        assertEquals (UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"), student.getId());
    }

    @Test
    public void testGetAll() {
        List<Student> students = studentService.getAll();
        assertEquals (4, students.size());
    }

    @Test
    public void testCreate() {
        Student createdStudent = studentService.create(createStudentDto);
        assertAll(
                () -> assertNotNull(createdStudent.getId()),
                () -> assertEquals(createdStudent.getFirstName(), createStudentDto.firstName()),
                () -> assertEquals(createdStudent.getLastName(), createStudentDto.lastName()),
                () -> assertEquals(createdStudent.getEmail(), createStudentDto.email()),
                () -> assertEquals(createdStudent.getDateOfBirth(), createStudentDto.dateOfBirth()),
                () -> assertEquals(createdStudent.getCoins(), createStudentDto.coins())
                );
    }

    @Test
    public void testUpdate() {
        Student updatedStudent = studentService.update(
                UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"),
                createStudentDto);
        int studentsAmount = studentService.getAll().size();
        assertAll(
                () -> assertEquals(4, studentsAmount),
                () -> assertEquals(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"), updatedStudent.getId()),
                () -> assertEquals(updatedStudent.getFirstName(), createStudentDto.firstName()),
                () -> assertEquals(updatedStudent.getLastName(), createStudentDto.lastName()),
                () -> assertEquals(updatedStudent.getEmail(), createStudentDto.email()),
                () -> assertEquals(updatedStudent.getDateOfBirth(), createStudentDto.dateOfBirth()),
                () -> assertEquals(updatedStudent.getCoins(), createStudentDto.coins())
        );
    }

    @Test
    public void testUpdateThrowsIncorrectResultSizeExceptionAndRollbacksTransaction() {
        Exception ex = assertThrows(IncorrectResultSizeException.class,
                () -> {
                    createStudentDto.courseIds().add(UUID.fromString("ddf747d2-f5de-4a2e-b8b9-66d8bd7e357e"));
                    studentService.update(
                            UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"),
                            createStudentDto);
                });
        assertEquals("Some of requested courses don't exist", ex.getMessage());

        Student student = studentService.getById(UUID.fromString("5a231280-1988-410f-98d9-852b8dc9caf1"));
        assertAll(
                () -> assertNotEquals(createStudentDto.firstName(), student.getFirstName()),
                () -> assertFalse(student.getCourses().stream()
                        .map(Course::getId)
                        .toList()
                        .contains(UUID.fromString("ddf747d2-f5de-4a2e-b8b9-66d8bd7e357e")))
        );
    }
}
