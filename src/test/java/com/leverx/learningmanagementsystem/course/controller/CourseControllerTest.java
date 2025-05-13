package com.leverx.learningmanagementsystem.course.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.course.dto.CreateCourseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private CreateCourseDto createCourseDto;

    @BeforeEach
    void setUpCreateCourseDto() {
        createCourseDto = new CreateCourseDto("Test course", "This is a test course",
                BigDecimal.valueOf(143), BigDecimal.valueOf(1430), null, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllCourses() throws Exception {
        var response = mockMvc.perform(get("/courses"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldReturnCourseAnd200() throws Exception {
        var response = mockMvc.perform(get("/courses/64852c52-ed64-4438-b095-2ca10f6b4be0"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$.title").value("Applied Math"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        var response = mockMvc.perform(get("/courses/2bcd9463-3c57-421b-91d0-047b315d60ce"));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Course not found [id = {2bcd9463-3c57-421b-91d0-047b315d60ce}]"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCreateCourseDto_shouldReturnCreatedCourseAnd201() throws Exception {
        var response = mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test course"))
                .andExpect(jsonPath("$.description").value("This is a test course"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturn204() throws Exception {
        var response = mockMvc.perform(delete("/courses/b902261b-d1b9-4c58-869f-b04a5bbff4c9"));

        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturn404() throws Exception {
        var response = mockMvc.perform(delete("/courses/d8b4bcc9-10d2-406d-ae74-7ee9b60522d2"));

        response.andExpect(status().isNotFound());
    }
}
