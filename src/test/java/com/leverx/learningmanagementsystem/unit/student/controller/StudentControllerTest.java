package com.leverx.learningmanagementsystem.unit.student.controller;

import com.leverx.learningmanagementsystem.student.controller.StudentController;
import com.leverx.learningmanagementsystem.student.dto.StudentResponseDto;
import com.leverx.learningmanagementsystem.student.webfacade.StudentWebFacade;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static testutils.StudentTestUtils.EXISTING_STUDENT_FIRST_NAME;
import static testutils.StudentTestUtils.EXISTING_STUDENT_ID;
import static testutils.StudentTestUtils.NON_EXISTING_STUDENT_ID;
import static testutils.StudentTestUtils.TOTAL_NUMBER_OF_STUDENTS;
import static testutils.StudentTestUtils.initializePage;
import static testutils.StudentTestUtils.initializeStudentResponseDto;
import static testutils.TestUtils.DEFAULT_PAGE;
import static testutils.TestUtils.PAGE;
import static testutils.TestUtils.PAGE_SIZE;

@WebMvcTest(StudentController.class)
@Tag("unit-test")
public class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StudentWebFacade studentWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllStudentsAnd200() throws Exception {
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_STUDENTS);
        Page page = initializePage(pageable);
        Mockito.when(studentWebFacade.getAll(pageable)).thenReturn(page);

        var response = this.mockMvc.perform(get("/students")
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_STUDENTS)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_STUDENTS))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_STUDENT_ID.toString()))
                .andExpect(jsonPath("$.content[1].id").value(EXISTING_STUDENT_ID.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnStudentAnd200() throws Exception {
        StudentResponseDto responseDto = initializeStudentResponseDto();
        Mockito.when(studentWebFacade.getById(EXISTING_STUDENT_ID)).thenReturn(responseDto);

        var response = this.mockMvc.perform(get("/students/" + EXISTING_STUDENT_ID));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_STUDENT_ID.toString()))
                .andExpect(jsonPath("$.firstName").value(EXISTING_STUDENT_FIRST_NAME));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        Mockito.when(studentWebFacade.getById(NON_EXISTING_STUDENT_ID))
                .thenThrow(new EntityNotFoundException(
                        "Student not found [id = {%s}]".formatted(NON_EXISTING_STUDENT_ID)
                ));

        var response = this.mockMvc.perform(get("/students/" + NON_EXISTING_STUDENT_ID));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value( "Student not found [id = {%s}]".formatted(NON_EXISTING_STUDENT_ID))
                );
    }
}
