package com.leverx.learningmanagementsystem.coursesettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leverx.learningmanagementsystem.AbstractIT;
import com.leverx.learningmanagementsystem.coursesettings.dto.CreateCourseSettingsDto;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils;

import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.COURSE_SETTINGS;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.EXISTING_COURSE_SETTINGS_END_DATE;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.EXISTING_COURSE_SETTINGS_ID;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.EXISTING_COURSE_SETTINGS_START_DATE;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.NEW_COURSE_SETTINGS_END_DATE;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.NEW_COURSE_SETTINGS_START_DATE;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.NON_EXISTING_COURSE_SETTINGS_ID;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.NUMBER_OF_COURSE_SETTINGS_FIELDS;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.TOTAL_NUMBER_OF_COURSE_SETTINGS;
import static com.leverx.learningmanagementsystem.utils.CourseSettingsITUtils.formatter;
import static com.leverx.learningmanagementsystem.utils.ITUtils.CLEAN_SQL;
import static com.leverx.learningmanagementsystem.utils.ITUtils.DEFAULT_PAGE;
import static com.leverx.learningmanagementsystem.utils.ITUtils.INSERT_SQL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseSettingsControllerIT extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getAll_shouldReturnAllCourseSettingsAnd200() throws Exception {
        // given
        var request = buildGetAllRequest(COURSE_SETTINGS, DEFAULT_PAGE, String.valueOf(TOTAL_NUMBER_OF_COURSE_SETTINGS));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(TOTAL_NUMBER_OF_COURSE_SETTINGS));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void getById_givenCourseSettingsId_shouldReturnCourseSettingsAnd200() throws Exception {
        // given
        var request = buildGetByIdRequest(COURSE_SETTINGS, String.valueOf(EXISTING_COURSE_SETTINGS_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_SETTINGS_FIELDS))
                .andExpect(jsonPath("$.id").value(EXISTING_COURSE_SETTINGS_ID.toString()))
                .andExpect(jsonPath("$.startDate").value(EXISTING_COURSE_SETTINGS_START_DATE.format(formatter)))
                .andExpect(jsonPath("$.endDate").value(EXISTING_COURSE_SETTINGS_END_DATE.format(formatter)));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void create_givenCreateCourseSettingsDto_shouldReturnCreatedCourseSettingsAnd201() throws Exception {
        // given
        CreateCourseSettingsDto newCourseSettings = CourseSettingsITUtils.initializeCreateCourseSettingsDto();
        var request = buildCreateRequest(COURSE_SETTINGS, APPLICATION_JSON, newCourseSettings);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_SETTINGS_FIELDS))
                .andExpect(jsonPath("$.startDate").value(NEW_COURSE_SETTINGS_START_DATE.format(formatter)))
                .andExpect(jsonPath("$.endDate").value(NEW_COURSE_SETTINGS_END_DATE.format(formatter)));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void updateById_givenCourseSettingsIdAndCreateCourseSettingsDto_shouldReturnUpdatedCourseSettingsAnd200() throws Exception {
        // given
        CreateCourseSettingsDto newCourseSettings = CourseSettingsITUtils.initializeCreateCourseSettingsDto();
        var request = buildUpdateByIdRequest(COURSE_SETTINGS, String.valueOf(EXISTING_COURSE_SETTINGS_ID),
                APPLICATION_JSON, newCourseSettings);

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(NUMBER_OF_COURSE_SETTINGS_FIELDS))
                .andExpect(jsonPath("$.startDate").value(NEW_COURSE_SETTINGS_START_DATE.format(formatter)))
                .andExpect(jsonPath("$.endDate").value(NEW_COURSE_SETTINGS_END_DATE.format(formatter)));
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnStatus204() throws Exception {
        // given
        var request = buildDeleteByIdRequest(COURSE_SETTINGS, String.valueOf(EXISTING_COURSE_SETTINGS_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    @Tag("integration")
    @Sql(scripts = {CLEAN_SQL, INSERT_SQL}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(roles = "USER")
    void delete_givenId_shouldReturnNotFound() throws Exception {
        // given
        var request = buildDeleteByIdRequest(COURSE_SETTINGS, String.valueOf(NON_EXISTING_COURSE_SETTINGS_ID));

        // when
        var response = mockMvc.perform(request);

        // then
        response.andExpect(status().isNotFound());
    }
}
