package com.leverx.learningmanagementsystem.student.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.student.dto.CreateStudentDto;
import com.leverx.learningmanagementsystem.testutils.StudentTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.EXISTING_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.EXISTING_STUDENT_LAST_NAME;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NEW_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NEW_STUDENT_LAST_NAME;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NON_EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.NUMBER_OF_STUDENT_FIELDS;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.STUDENTS;
import static com.leverx.learningmanagementsystem.testutils.StudentTestUtils.TOTAL_NUMBER_OF_STUDENTS;
import static com.leverx.learningmanagementsystem.testutils.TestUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.testutils.TestUtils.INSERT_SQL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles="USER")
    void getAll_shouldReturnAllStudentsAnd200() throws Exception{
        var response = mockMvc.perform(get(STUDENTS));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(TOTAL_NUMBER_OF_STUDENTS))
                .andExpect(jsonPath("$[0].firstName").value(EXISTING_STUDENT_FIRST_NAME))
                .andExpect(jsonPath("$[0].lastName").value(EXISTING_STUDENT_LAST_NAME));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles="USER")
    void getById_GivenId_shouldReturnStudentAnd200() throws Exception{
        var response = mockMvc.perform(get(STUDENTS + "/" + EXISTING_STUDENT_ID));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_STUDENT_FIELDS))
                .andExpect(jsonPath("$.firstName").value(EXISTING_STUDENT_FIRST_NAME));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles="USER")
    void getById_givenId_shouldReturnEntityNotFoundExceptionAnd404() throws Exception{
        var response = mockMvc.perform(get(STUDENTS + "/" + NON_EXISTING_STUDENT_ID));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Student not found [id = {%s}]".formatted(NON_EXISTING_STUDENT_ID)));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles="USER")
    void create_givenCreateStudentDto_shouldReturnCreatedStudentAnd201() throws Exception{
        CreateStudentDto newStudent = StudentTestUtils.initializeCreateStudentDto();

        var response = mockMvc.perform(post(STUDENTS)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(NEW_STUDENT_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(NEW_STUDENT_LAST_NAME));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles="USER")
    void delete_givenId_shouldReturn204() throws Exception{
        var response = mockMvc.perform(delete(STUDENTS + "/" + EXISTING_STUDENT_ID));

        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles="USER")
    void delete_givenId_shouldReturn404() throws Exception{
        var response = mockMvc.perform(delete(STUDENTS + "/" + NON_EXISTING_STUDENT_ID));

        response.andExpect(status().isNotFound());
    }
}