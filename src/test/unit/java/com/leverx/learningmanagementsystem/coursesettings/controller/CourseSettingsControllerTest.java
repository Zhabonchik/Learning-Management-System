package com.leverx.learningmanagementsystem.coursesettings.controller;

import com.leverx.learningmanagementsystem.coursesettings.dto.CourseSettingsResponseDto;
import com.leverx.learningmanagementsystem.coursesettings.webfacade.CourseSettingsWebFacade;
import com.leverx.learningmanagementsystem.utils.exception.model.EntityNotFoundException;
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

import static com.leverx.learningmanagementsystem.utils.CourseSettingsTestUtils.EXISTING_COURSE_SETTINGS_ID;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsTestUtils.NON_EXISTING_COURSE_SETTINGS_ID;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsTestUtils.TOTAL_NUMBER_OF_COURSE_SETTINGS;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsTestUtils.initializeCourseSettingsResponseDto;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsTestUtils.initializePage;
import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.NON_EXISTING_COURSE_ID;
import static com.leverx.learningmanagementsystem.utils.CourseTestUtils.TOTAL_NUMBER_OF_COURSES;
import static com.leverx.learningmanagementsystem.utils.TestUtils.CREATED;
import static com.leverx.learningmanagementsystem.utils.TestUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE;
import static com.leverx.learningmanagementsystem.utils.TestUtils.PAGE_SIZE;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CourseSettingsController.class)
@Tag("unit")
public class CourseSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseSettingsWebFacade courseSettingsWebFacade;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllCourseSettingsAnd200() throws Exception {
        // given
        Pageable pageable = PageRequest.of(Integer.parseInt(DEFAULT_PAGE), TOTAL_NUMBER_OF_COURSES, Sort.by(CREATED));
        Page<CourseSettingsResponseDto> page = initializePage(pageable);
        when(courseSettingsWebFacade.getAll(pageable)).thenReturn(page);

        // when
        var response = mockMvc.perform(get("/course-settings")
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_COURSE_SETTINGS)));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_COURSES))
                .andExpect(jsonPath("$.content[0].id").value(EXISTING_COURSE_SETTINGS_ID.toString()))
                .andExpect(jsonPath("$.content[1].id").value(EXISTING_COURSE_SETTINGS_ID.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenCourseSettingsId_shouldReturnCourseSettingsAnd200() throws Exception {
        // given
        CourseSettingsResponseDto responseDto = initializeCourseSettingsResponseDto();
        when(courseSettingsWebFacade.getById(EXISTING_COURSE_SETTINGS_ID)).thenReturn(responseDto);

        // when
        var response = mockMvc.perform(get("/course-settings/" + EXISTING_COURSE_SETTINGS_ID));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(EXISTING_COURSE_SETTINGS_ID.toString()));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getById_givenId_shouldThrowEntityNotFoundExceptionAnd404() throws Exception {
        // given
        when(courseSettingsWebFacade.getById(NON_EXISTING_COURSE_SETTINGS_ID))
                .thenThrow(new EntityNotFoundException(
                        "Course settings not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)
                ));

        // when
        var response = mockMvc.perform(get("/course-settings/" + NON_EXISTING_COURSE_SETTINGS_ID));

        //then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(
                        "Course settings not found [id = {%s}]".formatted(NON_EXISTING_COURSE_ID)
                ));
    }

}
