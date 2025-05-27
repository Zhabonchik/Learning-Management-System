package com.leverx.learningmanagementsystem.unit.course.controller;

import com.leverx.learningmanagementsystem.course.controller.CourseController;
import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.webfacade.CourseWebFacade;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
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
import static testutils.CourseTestUtils.EXISTING_COURSE_ID;
import static testutils.CourseTestUtils.EXISTING_COURSE_TITLE;
import static testutils.CourseTestUtils.NON_EXISTING_COURSE_ID;
import static testutils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static testutils.CourseTestUtils.initializeCourseResponseDto;
import static testutils.CourseTestUtils.initializePage;
import static testutils.TestUtils.DEFAULT_PAGE;
import static testutils.TestUtils.PAGE;
import static testutils.TestUtils.PAGE_SIZE;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseWebFacade courseWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllCourses() throws Exception {
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_COURSES);
        Page<CourseResponseDto> page = initializePage(pageable);
        Mockito.when(courseWebFacade.getAll(pageable)).thenReturn(page);

        var response = this.mockMvc.perform(get("/courses")
                .param(PAGE, String.valueOf(DEFAULT_PAGE))
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_COURSES)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(TOTAL_NUMBER_OF_COURSES))
                .andExpect(jsonPath("$.content[0].title").value(EXISTING_COURSE_TITLE))
                .andExpect(jsonPath("$.content[1].title").value(EXISTING_COURSE_TITLE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnCourseAnd200() throws Exception {
        CourseResponseDto responseDto = initializeCourseResponseDto();
        Mockito.when(courseWebFacade.getById(EXISTING_COURSE_ID)).thenReturn(responseDto);

        var response = this.mockMvc.perform(get("/courses/" + EXISTING_COURSE_ID));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_COURSE_ID.toString()))
                .andExpect(jsonPath("$.title").value(EXISTING_COURSE_TITLE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        Mockito.when(courseWebFacade.getById(NON_EXISTING_COURSE_ID))
                .thenThrow(new EntityNotFoundException("Course not found [id = {%s}]"
                        .formatted(NON_EXISTING_COURSE_ID)));

        var response = this.mockMvc.perform(get("/courses/" + NON_EXISTING_COURSE_ID));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Course not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)));
    }
}

