package com.leverx.learningmanagementsystem.integration.coursesettings.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;
import testutils.CourseSettingsTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static testutils.CourseSettingsTestUtils.COURSE_SETTINGS;
import static testutils.CourseSettingsTestUtils.EXISTING_COURSE_SETTINGS_END_DATE;
import static testutils.CourseSettingsTestUtils.EXISTING_COURSE_SETTINGS_ID;
import static testutils.CourseSettingsTestUtils.EXISTING_COURSE_SETTINGS_START_DATE;
import static testutils.CourseSettingsTestUtils.NEW_COURSE_SETTINGS_END_DATE;
import static testutils.CourseSettingsTestUtils.NEW_COURSE_SETTINGS_START_DATE;
import static testutils.CourseSettingsTestUtils.NON_EXISTING_COURSE_SETTINGS_ID;
import static testutils.CourseSettingsTestUtils.NUMBER_OF_COURSE_SETTINGS_FIELDS;
import static testutils.CourseSettingsTestUtils.TOTAL_NUMBER_OF_COURSE_SETTINGS;
import static testutils.CourseSettingsTestUtils.formatter;
import static testutils.TestUtils.CLEAN_SQL;
import static testutils.TestUtils.DEFAULT_PAGE;
import static testutils.TestUtils.INSERT_SQL;
import static testutils.TestUtils.PAGE;
import static testutils.TestUtils.PAGE_SIZE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class CourseSettingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllCourseSettingsAnd200() throws Exception {
        var response = mockMvc.perform(get(COURSE_SETTINGS)
                .param(PAGE, DEFAULT_PAGE)
                .param(PAGE_SIZE, String.valueOf(TOTAL_NUMBER_OF_COURSE_SETTINGS)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_COURSE_SETTINGS));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_givenCourseSettingsId_shouldReturnCourseSettingsAnd200() throws Exception {
        var response = mockMvc.perform(get(COURSE_SETTINGS + "/" + EXISTING_COURSE_SETTINGS_ID));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_SETTINGS_FIELDS))
                .andExpect(jsonPath("$.id").value(EXISTING_COURSE_SETTINGS_ID.toString()))
                .andExpect(jsonPath("$.startDate").value(EXISTING_COURSE_SETTINGS_START_DATE.format(formatter)))
                .andExpect(jsonPath("$.endDate").value(EXISTING_COURSE_SETTINGS_END_DATE.format(formatter)));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenCreateCourseSettingsDto_shouldReturnCreatedCourseSettingsAnd201() throws Exception {
        CreateCourseSettingsDto newCourseSettings = CourseSettingsTestUtils.initializeCreateCourseSettingsDto();

        var response = mockMvc.perform(post(COURSE_SETTINGS)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseSettings)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_SETTINGS_FIELDS))
                .andExpect(jsonPath("$.startDate").value(NEW_COURSE_SETTINGS_START_DATE.format(formatter)))
                .andExpect(jsonPath("$.endDate").value(NEW_COURSE_SETTINGS_END_DATE.format(formatter)));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void updateById_givenCourseSettingsIdAndCreateCourseSettingsDto_shouldReturnUpdatedCourseSettingsAnd200() throws Exception {
        CreateCourseSettingsDto newCourseSettings = CourseSettingsTestUtils.initializeCreateCourseSettingsDto();

        var response = mockMvc.perform(put(COURSE_SETTINGS + "/" + EXISTING_COURSE_SETTINGS_ID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourseSettings)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_SETTINGS_FIELDS))
                .andExpect(jsonPath("$.startDate").value(NEW_COURSE_SETTINGS_START_DATE.format(formatter)))
                .andExpect(jsonPath("$.endDate").value(NEW_COURSE_SETTINGS_END_DATE.format(formatter)));
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnStatus204() throws Exception {
        var response = mockMvc.perform(delete(COURSE_SETTINGS + "/" + EXISTING_COURSE_SETTINGS_ID));

        response.andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnNotFound() throws Exception {
        var response = mockMvc.perform(delete(COURSE_SETTINGS + "/" + NON_EXISTING_COURSE_SETTINGS_ID));

        response.andExpect(status().isNotFound());
    }
}
