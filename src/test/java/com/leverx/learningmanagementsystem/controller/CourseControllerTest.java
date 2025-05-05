package com.leverx.learningmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static CreateCourseDto createCourseDto;

    @BeforeAll
    public static void setUp() {
        createCourseDto = new CreateCourseDto("Test course", "This is a test course",
                BigDecimal.valueOf(143), BigDecimal.valueOf(1430), null, new ArrayList<>(), new ArrayList<>());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetById() throws Exception {
        mockMvc.perform(get("/courses/64852c52-ed64-4438-b095-2ca10f6b4be0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$.title").value("Applied Math"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetByIdThrowsEntityNotFoundException() throws Exception{
        mockMvc.perform(get("/courses/2bcd9463-3c57-421b-91d0-047b315d60ce"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Course not found [id = {2bcd9463-3c57-421b-91d0-047b315d60ce}]"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testCreate() throws Exception{
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test course"))
                .andExpect(jsonPath("$.description").value("This is a test course"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDelete() throws Exception{
        mockMvc.perform(delete("/courses/b902261b-d1b9-4c58-869f-b04a5bbff4c9"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteNotFound() throws Exception{
        mockMvc.perform(delete("/courses/d8b4bcc9-10d2-406d-ae74-7ee9b60522d2"))
                .andExpect(status().isNotFound());
    }
}
