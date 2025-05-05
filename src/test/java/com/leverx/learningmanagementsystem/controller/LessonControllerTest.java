package com.leverx.learningmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.dto.lesson.CreateLessonDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private CreateLessonDto createLessonDto;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        createLessonDto = new CreateLessonDto("Test Lesson",80,
                UUID.fromString("64852c52-ed64-4438-b095-2ca10f6b4be0"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllLessonsAnd200() throws Exception {
        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenLessonId_shouldReturnLessonAnd200() throws Exception {
        mockMvc.perform(get("/lessons/373e60bb-c872-4207-982c-859b4dfdb4f7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.id").value("373e60bb-c872-4207-982c-859b4dfdb4f7"))
                .andExpect(jsonPath("$.title").value("Linear algebra"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCreateLessonDto_ShouldReturnCreatedLessonAnd201() throws Exception {
        mockMvc.perform(post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createLessonDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.title").value("Test Lesson"))
                .andExpect(jsonPath("$.courseId").value("64852c52-ed64-4438-b095-2ca10f6b4be0"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateById_givenLessonIdAndCreateLessonDto_ShouldReturnUpdatedLessonAnd200() throws Exception {
        mockMvc.perform(put("/lessons/373e60bb-c872-4207-982c-859b4dfdb4f7")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createLessonDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.title").value("Test Lesson"))
                .andExpect(jsonPath("$.courseId").value("64852c52-ed64-4438-b095-2ca10f6b4be0"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_ShouldReturnStatus204() throws Exception {
        mockMvc.perform(delete("/lessons/373e60bb-c872-4207-982c-859b4dfdb4f7"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/lessons/891eed08-bc3a-4f57-85f5-bd1fb8b6eed6"))
                .andExpect(status().isNotFound());
    }
}
