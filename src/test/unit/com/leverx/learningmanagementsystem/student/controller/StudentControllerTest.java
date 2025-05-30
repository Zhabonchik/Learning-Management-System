package com.leverx.learningmanagementsystem.student.controller;

import com.leverx.learningmanagementsystem.student.dto.StudentResponseDto;
import com.leverx.learningmanagementsystem.student.facade.StudentWebFacade;
import com.leverx.learningmanagementsystem.core.exception.model.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leverx.learningmanagementsystem.utils.StudentTestUtils.EXISTING_STUDENT_FIRST_NAME;
import static com.leverx.learningmanagementsystem.utils.StudentTestUtils.EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.utils.StudentTestUtils.NON_EXISTING_STUDENT_ID;
import static com.leverx.learningmanagementsystem.utils.StudentTestUtils.STUDENTS;
import static com.leverx.learningmanagementsystem.utils.StudentTestUtils.TOTAL_NUMBER_OF_STUDENTS;
import static com.leverx.learningmanagementsystem.utils.StudentTestUtils.initializePage;
import static com.leverx.learningmanagementsystem.utils.StudentTestUtils.initializeStudentResponseDto;
import static com.leverx.learningmanagementsystem.utils.TestUtils.CREATED;
import static com.leverx.learningmanagementsystem.utils.TestUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE_SIZE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@Tag("unit")
public class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    StudentWebFacade studentWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllStudentsAnd200() throws Exception {
        // given
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_STUDENTS, Sort.by(CREATED));
        Page page = initializePage(pageable);
        when(studentWebFacade.getAll(pageable)).thenReturn(page);

        // when
        var response = this.mockMvc.perform(get(STUDENTS)
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_STUDENTS)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_STUDENTS))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_STUDENT_ID.toString()))
                .andExpect(jsonPath("$.content[1].id").value(EXISTING_STUDENT_ID.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnStudentAnd200() throws Exception {
        // given
        StudentResponseDto responseDto = initializeStudentResponseDto();
        when(studentWebFacade.getById(EXISTING_STUDENT_ID)).thenReturn(responseDto);

        // when
        var response = this.mockMvc.perform(get(STUDENTS + "/" + EXISTING_STUDENT_ID));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_STUDENT_ID.toString()))
                .andExpect(jsonPath("$.firstName").value(EXISTING_STUDENT_FIRST_NAME));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        // given
        when(studentWebFacade.getById(NON_EXISTING_STUDENT_ID))
                .thenThrow(new EntityNotFoundException(
                        "Student not found [id = {%s}]".formatted(NON_EXISTING_STUDENT_ID)
                ));

        // when
        var response = this.mockMvc.perform(get(STUDENTS + "/" + NON_EXISTING_STUDENT_ID));

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Student not found [id = {%s}]".formatted(NON_EXISTING_STUDENT_ID))
                );
    }
}
