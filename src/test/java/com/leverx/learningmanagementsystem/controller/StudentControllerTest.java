package com.leverx.learningmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.dto.student.CreateStudentDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static CreateStudentDto createStudentDto;

    @BeforeAll
    static void setUp() {
        createStudentDto = new CreateStudentDto("A", "B", "email@gmail.com",
                LocalDate.of(2005, 7, 23), new BigDecimal(1548), new ArrayList<>());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetAll() throws Exception{
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Abap"))
                .andExpect(jsonPath("$[1].firstName").value("Ha"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetById() throws Exception{
        mockMvc.perform(get("/students/5a231280-1988-410f-98d9-852b8dc9caf1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(7))
                .andExpect(jsonPath("$.firstName").value("Abap"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetByIdThrowsEntityNotFoundException() throws Exception{
        mockMvc.perform(get("/students/2bcd9463-3c57-421b-91d0-047b315d60ce"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Student not found [id = {2bcd9463-3c57-421b-91d0-047b315d60ce}]"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testCreate() throws Exception{
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createStudentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("A"))
                .andExpect(jsonPath("$.lastName").value("B"));
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDelete() throws Exception{
        mockMvc.perform(delete("/students/8ce93381-f58d-4563-8866-34a0ed878c74"))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {"/sql/clean-db.sql", "/sql/insert-test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteNotFound() throws Exception{
        mockMvc.perform(delete("/students/ccf99c5d-9ce8-45c4-aaa7-c936baa51415"))
                .andExpect(status().isNotFound());
    }
}