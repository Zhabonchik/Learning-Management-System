package com.leverx.learningmanagementsystem.course.controller;

import com.leverx.learningmanagementsystem.course.dto.CourseResponseDto;
import com.leverx.learningmanagementsystem.course.webfacade.CourseWebFacade;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.EXISTING_COURSE_TITLE;
import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.NON_EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.initializeCourseResponseDto;
import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.initializePage;
import static com.leverx.learningmanagementsystem.utils.TestUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE_SIZE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@Tag("unit-test")
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseWebFacade courseWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllCourses() throws Exception {
        // given
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_COURSES);
        Page<CourseResponseDto> page = initializePage(pageable);
        when(courseWebFacade.getAll(pageable)).thenReturn(page);

        // when
        var response = this.mockMvc.perform(get("/courses")
                .param(PAGE, String.valueOf(DEFAULT_PAGE))
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_COURSES)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(TOTAL_NUMBER_OF_COURSES))
                .andExpect(jsonPath("$.content[0].title").value(EXISTING_COURSE_TITLE))
                .andExpect(jsonPath("$.content[1].title").value(EXISTING_COURSE_TITLE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldReturnCourseAnd200() throws Exception {
        // given
        CourseResponseDto responseDto = initializeCourseResponseDto();
        when(courseWebFacade.getById(EXISTING_COURSE_ID)).thenReturn(responseDto);

        //when
        var response = this.mockMvc.perform(get("/courses/" + EXISTING_COURSE_ID));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_COURSE_ID.toString()))
                .andExpect(jsonPath("$.title").value(EXISTING_COURSE_TITLE));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        // given
        when(courseWebFacade.getById(NON_EXISTING_COURSE_ID))
                .thenThrow(new EntityNotFoundException("Course not found [id = {%s}]"
                        .formatted(NON_EXISTING_COURSE_ID)));

        // when
        var response = this.mockMvc.perform(get("/courses/" + NON_EXISTING_COURSE_ID));

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Course not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)));
    }
}

