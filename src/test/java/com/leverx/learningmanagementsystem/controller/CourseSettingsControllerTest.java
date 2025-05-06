package com.leverx.learningmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.dto.coursesettings.CreateCourseSettingsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private CreateCourseSettingsDto createCourseSettingsDto;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        createCourseSettingsDto = new CreateCourseSettingsDto(
                LocalDateTime.of(2026,5, 6, 10,0,0),
                LocalDateTime.of(2026,6, 1, 10,0,0),true);
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getAll_shouldReturnAllCourseSettingsAnd200() throws Exception {
        mockMvc.perform(get("/course-settings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getById_givenCourseSettingsId_shouldReturnCourseSettingsAnd200() throws Exception {
        mockMvc.perform(get("/course-settings/38811913-ce40-4ef4-a596-f24d94356949"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.id").value("38811913-ce40-4ef4-a596-f24d94356949"))
                .andExpect(jsonPath("$.startDate").value("2025-05-13 09:30:00"))
                .andExpect(jsonPath("$.endDate").value("2025-06-23 15:00:00"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void create_givenCreateCourseSettingsDto_shouldReturnCreatedCourseSettingsAnd201() throws Exception {
        mockMvc.perform(post("/course-settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseSettingsDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.startDate").value("2026-05-06 10:00:00"))
                .andExpect(jsonPath("$.endDate").value("2026-06-01 10:00:00"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updateById_givenCourseSettingsIdAndCreateCourseSettingsDto_shouldReturnUpdatedCourseSettingsAnd200() throws Exception {
        mockMvc.perform(put("/course-settings/34b56d95-5d74-44b1-b473-3ab2fd36cc5f")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseSettingsDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.startDate").value("2026-05-06 10:00:00"))
                .andExpect(jsonPath("$.endDate").value("2026-06-01 10:00:00"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturnStatus204() throws Exception {
        mockMvc.perform(delete("/course-settings/34b56d95-5d74-44b1-b473-3ab2fd36cc5f"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void delete_givenId_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/course-settings/891eed08-bc3a-4f57-85f5-bd1fb8b6eed6"))
                .andExpect(status().isNotFound());
    }
}
