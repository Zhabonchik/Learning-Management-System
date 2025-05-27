package com.leverx.learningmanagementsystem.unit.coursesettings.controller;

import com.leverx.learningmanagementsystem.coursesettings.controller.CourseSettingsController;
import com.leverx.learningmanagementsystem.coursesettings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.coursesettings.webfacade.CourseSettingsWebFacade;
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
import static testutils.CourseSettingsTestUtils.EXISTING_COURSE_SETTINGS_ID;
import static testutils.CourseSettingsTestUtils.NON_EXISTING_COURSE_SETTINGS_ID;
import static testutils.CourseSettingsTestUtils.TOTAL_NUMBER_OF_COURSE_SETTINGS;
import static testutils.CourseSettingsTestUtils.initializeCourseSettingsResponseDto;
import static testutils.CourseSettingsTestUtils.initializePage;
import static testutils.CourseTestUtils.NON_EXISTING_COURSE_ID;
import static testutils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static testutils.TestUtils.DEFAULT_PAGE;
import static testutils.TestUtils.PAGE;
import static testutils.TestUtils.PAGE_SIZE;

@WebMvcTest(controllers = CourseSettingsController.class)
public class CourseSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseSettingsWebFacade courseSettingsWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllCourseSettingsAnd200() throws Exception {
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_COURSES);
        Page<CourseSettingsResponseDto> page = initializePage(pageable);
        Mockito.when(courseSettingsWebFacade.getAll(pageable)).thenReturn(page);

        var response = mockMvc.perform(get("/course-settings")
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_COURSE_SETTINGS)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_COURSES))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_COURSE_SETTINGS_ID.toString()))
                .andExpect(jsonPath("$.content[1].id").value(EXISTING_COURSE_SETTINGS_ID.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenCourseSettingsId_shouldReturnCourseSettingsAnd200() throws Exception {
        CourseSettingsResponseDto responseDto = initializeCourseSettingsResponseDto();
        Mockito.when(courseSettingsWebFacade.getById(EXISTING_COURSE_SETTINGS_ID)).thenReturn(responseDto);

        var response = mockMvc.perform(get("/course-settings/" + EXISTING_COURSE_SETTINGS_ID));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_COURSE_SETTINGS_ID.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        Mockito.when(courseSettingsWebFacade.getById(NON_EXISTING_COURSE_SETTINGS_ID))
                .thenThrow(new EntityNotFoundException(
                        "Course settings not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)
                ));

        var response = mockMvc.perform(get("/course-settings/" + NON_EXISTING_COURSE_SETTINGS_ID));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Course settings not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)
                ));
    }

}
