package com.leverx.learningmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static CreateStudentDto createStudentDto;

    @BeforeAll
    public static void setUp() {
        createStudentDto = new CreateStudentDto("A", "B", "email@gmail.com",
                LocalDate.of(2005, 7, 23), new BigDecimal(1548), new ArrayList<>());
    }

    @Test
    void testGetAll() throws Exception{
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].firstName").value("Abap"))
                .andExpect(jsonPath("$[1].firstName").value("Ha"))
                .andExpect(jsonPath("$[2].firstName").value("Hah"))
                .andExpect(jsonPath("$[3].firstName").value("Haha"));
    }

    @Test
    void testGetById() throws Exception{
        mockMvc.perform(get("/students/5a231280-1988-410f-98d9-852b8dc9caf1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$.firstName").value("Abap"));
    }

    @Test
    void testGetByIdThrowsEntityNotFoundException() throws Exception{
        mockMvc.perform(get("/students/2bcd9463-3c57-421b-91d0-047b315d60ce"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Student with id 2bcd9463-3c57-421b-91d0-047b315d60ce not found"));
    }

    @Test
    @Transactional
    void testCreate() throws Exception{
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStudentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("A"))
                .andExpect(jsonPath("$.lastName").value("B"));
    }

    @Test
    @Transactional
    void testDelete() throws Exception{
        mockMvc.perform(delete("/students/ccb8634d-af2e-4681-aa02-221f4dbf37a7"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    void testDeleteNotFound() throws Exception{
        mockMvc.perform(delete("/students/ccf99c5d-9ce8-45c4-aaa7-c936baa51415"))
                .andExpect(status().isNotFound());
    }
}