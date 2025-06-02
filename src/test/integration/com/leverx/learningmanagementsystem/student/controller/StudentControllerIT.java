package com.leverx.learningmanagementsystem.student.controller;

import com.leverx.learningmanagementsystem.AbstractIT;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudentControllerIT extends AbstractIT {

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllStudentsAnd200() throws Exception {
        // given
        var request = buildGetAllRequest(STUDENTS, DEFAULT_PAGE, String.valueOf(TOTAL_NUMBER_OF_STUDENTS));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_STUDENTS))
                .andExpect(jsonPath("$.content[0].firstName").value(EXISTING_STUDENT_FIRST_NAME))
                .andExpect(jsonPath("$.content[0].lastName").value(EXISTING_STUDENT_LAST_NAME));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_GivenId_shouldReturnStudentAnd200() throws Exception {
        // given
        var request = buildGetByIdRequest(STUDENTS, String.valueOf(EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_STUDENT_FIELDS))
                .andExpect(jsonPath("$.firstName").value(EXISTING_STUDENT_FIRST_NAME));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnEntityNotFoundExceptionAnd404() throws Exception {
        // given
        var request = buildGetByIdRequest(STUDENTS, String.valueOf(NON_EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Student not found [id = {%s}]".formatted(NON_EXISTING_STUDENT_ID)));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenCreateStudentDto_shouldReturnCreatedStudentAnd201() throws Exception {
        // given
        CreateStudentDto newStudent = initializeCreateStudentDto();
        var request = buildCreateRequest(STUDENTS, APPLICATION_JSON, newStudent);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(NEW_STUDENT_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(NEW_STUDENT_LAST_NAME));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturn204() throws Exception {
        // given
        var request = buildDeleteByIdRequest(STUDENTS, String.valueOf(EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturn404() throws Exception {
        // given
        var request = buildDeleteByIdRequest(STUDENTS, String.valueOf(NON_EXISTING_STUDENT_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound());
    }
}