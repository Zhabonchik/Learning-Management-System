package com.leverx.learningmanagementsystem.student.service;

import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.student.repository.StudentRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static com.leverx.learningmanagementsystem.student.common.utils.StudentTestUtils.EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.student.common.utils.StudentTestUtils.NEW_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.student.common.utils.StudentTestUtils.NEW_STUDENT_LAST_NAME;
import static com.leverx.learningmanagementsystem.student.common.utils.StudentTestUtils.TOTAL_NUMBER_OF_STUDENTS;
import static com.leverx.learningmanagementsystem.student.common.utils.StudentTestUtils.initializeStudent;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class StudentServiceTest {

    @Mock
    StudentRepository studentRepository;

    @InjectMocks
    StudentServiceImpl studentService;

    @Test
    void getAll_shouldReturnAllStudents() {
        // given
        Student firstStudent = initializeStudent();
        Student secondStudent = initializeStudent();
        when(studentRepository.findAll()).thenReturn(List.of(firstStudent, secondStudent));

        // when
        var students = studentService.getAll();

        // then
        assertAll(
                () -> assertEquals(TOTAL_NUMBER_OF_STUDENTS, students.size()),
                () -> assertEquals(NEW_STUDENT_FIRST_NAME, students.get(0).getFirstName()),
                () -> assertEquals(NEW_STUDENT_FIRST_NAME, students.get(1).getFirstName())
        );
    }

    @Test
    void getById_givenId_shouldReturnStudent() {
        // given
        Student existingStudent = initializeStudent();
        existingStudent.setId(EXISTING_STUDENT_ID);
        when(studentRepository.findById(EXISTING_STUDENT_ID)).thenReturn(Optional.of(existingStudent));

        // when
        var student = studentService.getById(EXISTING_STUDENT_ID);

        // then
        assertEquals(EXISTING_STUDENT_ID, student.getId());
    }

    @Test
    void create_givenStudent_shouldReturnStudent() {
        // given
        Student newStudent = initializeStudent();
        when(studentRepository.findById(EXISTING_STUDENT_ID)).thenReturn(Optional.of(newStudent));

        // when
        var student = studentService.getById(EXISTING_STUDENT_ID);

        // then
        assertAll(
                () -> assertEquals(NEW_STUDENT_FIRST_NAME, student.getFirstName()),
                () -> assertEquals(NEW_STUDENT_LAST_NAME, student.getLastName())
        );
    }

}
