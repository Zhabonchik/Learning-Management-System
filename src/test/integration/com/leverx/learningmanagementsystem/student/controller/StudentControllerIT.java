package com.leverx.learningmanagementsystem.student.controller;

import com.leverx.learningmanagementsystem.AbstractCommonIT;
import com.leverx.learningmanagementsystem.course.model.Course;
import com.leverx.learningmanagementsystem.course.service.CourseService;
import com.leverx.learningmanagementsystem.student.builder.StudentRequestBuilder;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.student.model.Student;
import com.leverx.learningmanagementsystem.student.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static com.leverx.learningmanagementsystem.utils.CourseITUtils.COURSES;
import static com.leverx.learningmanagementsystem.utils.CourseITUtils.EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.EXISTING_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.EXISTING_STUDENT_LAST_NAME;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NEW_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NEW_STUDENT_LAST_NAME;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NON_EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.NUMBER_OF_STUDENT_FIELDS;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.STUDENTS;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.TOTAL_NUMBER_OF_STUDENTS;
import static com.leverx.learningmanagementsystem.utils.StudentITUtils.initializeCreateStudentDto;
import static com.leverx.learningmanagementsystem.utils.ITUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.utils.ITUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.utils.ITUtils.INSERT_SQL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudentControllerIT extends AbstractCommonIT {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentRequestBuilder requestBuilder;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllStudentsAnd200() throws Exception {
        // given
        var request = requestBuilder.buildGetAllRequest(STUDENTS, DEFAULT_PAGE, String.valueOf(TOTAL_NUMBER_OF_STUDENTS));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.studentResponseDtoList.size()").value(TOTAL_NUMBER_OF_STUDENTS))
                .andExpect(jsonPath("$._embedded.studentResponseDtoList[0].firstName").value(EXISTING_STUDENT_FIRST_NAME))
                .andExpect(jsonPath("$._embedded.studentResponseDtoList[0].lastName").value(EXISTING_STUDENT_LAST_NAME));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_GivenId_shouldReturnStudentAnd200() throws Exception {
        // given
        var request = requestBuilder.buildGetByIdRequest(STUDENTS, String.valueOf(EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_STUDENT_FIELDS))
                .andExpect(jsonPath("$.firstName").value(EXISTING_STUDENT_FIRST_NAME));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnEntityNotFoundExceptionAnd404() throws Exception {
        // given
        var request = requestBuilder.buildGetByIdRequest(STUDENTS, String.valueOf(NON_EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Student not found [id = {%s}]".formatted(NON_EXISTING_STUDENT_ID)));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCreateStudentDto_shouldReturnCreatedStudentAnd201() throws Exception {
        // given
        CreateStudentDto newStudent = initializeCreateStudentDto();
        var request = requestBuilder.buildCreateRequest(STUDENTS, APPLICATION_JSON, newStudent);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(NEW_STUDENT_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(NEW_STUDENT_LAST_NAME));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void addCourse_givenStudentIdAndCourseId_shouldEnrollStudentForCourseAndReturn204() throws Exception {
        // given
        Student studentWithoutCourse = studentService.getById(EXISTING_STUDENT_ID);
        Course courseWithoutStudent = courseService.getById(EXISTING_COURSE_ID);
        var request = requestBuilder.buildPostRequest(STUDENTS + "/" + EXISTING_STUDENT_ID + COURSES + "/" + EXISTING_COURSE_ID);

        // when
        var response = mockMvc.perform(request);

        // then
        Student studentWithCourse = studentService.getById(EXISTING_STUDENT_ID);
        Course courseWithStudent = courseService.getById(EXISTING_COURSE_ID);
        response.andExpect(status().isCreated());
        assertAll(
                () -> assertTrue(studentWithCourse.getCourses().stream().map(Course::getId).toList().contains(EXISTING_COURSE_ID)),
                () -> assertTrue(courseWithStudent.getStudents().stream().map(Student::getId).toList().contains(EXISTING_STUDENT_ID)),
                () -> assertEquals(courseWithoutStudent.getCoinsPaid().add(courseWithoutStudent.getPrice()), courseWithStudent.getCoinsPaid()),
                () -> assertEquals(studentWithoutCourse.getCoins().subtract(courseWithoutStudent.getPrice()), studentWithCourse.getCoins())
        );
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturn204() throws Exception {
        // given
        var request = requestBuilder.buildDeleteByIdRequest(STUDENTS, String.valueOf(EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturn404() throws Exception {
        // given
        var request = requestBuilder.buildDeleteByIdRequest(STUDENTS, String.valueOf(NON_EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound());
    }
}