package com.leverx.learningmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.dto.course.CreateCourseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    public void testGetAll() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    public void testGetById() throws Exception {
        mockMvc.perform(get("/courses/f74405a1-25e9-4b19-b366-bf4be157d138"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(8))
                .andExpect(jsonPath("$.title").value("Test course 5"));
    }

    @Test
    void testGetByIdThrowsEntityNotFoundException() throws Exception{
        mockMvc.perform(get("/courses/2bcd9463-3c57-421b-91d0-047b315d60ce"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Course with id = 2bcd9463-3c57-421b-91d0-047b315d60ce not found"));
    }

    @Test
    @Transactional
    void testCreate() throws Exception{
        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test course"))
                .andExpect(jsonPath("$.description").value("This is a test course"));
    }

    @Test
    @Transactional
    void testDelete() throws Exception{
        mockMvc.perform(delete("/courses/df1af879-883c-47d7-a827-6a2a21ba0c70"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void testDeleteNotFound() throws Exception{
        mockMvc.perform(delete("/courses/d8b4bcc9-10d2-406d-ae74-7ee9b60522d2"))
                .andExpect(status().isNotFound());
    }
}
